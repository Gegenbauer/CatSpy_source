package me.gegenbauer.catspy.task

import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.async
import kotlinx.coroutines.channels.BufferOverflow
import kotlinx.coroutines.channels.ProducerScope
import kotlinx.coroutines.flow.*
import me.gegenbauer.catspy.concurrency.CancellablePause
import me.gegenbauer.catspy.log.GLog
import java.io.BufferedInputStream
import java.io.File
import java.util.*

// TODO 增加关于接受输出和处理输出的速度比较，避免出现数据积压然后遗漏的情况
abstract class CommandTask(
    protected val commands: Array<String>,
    private val args: Array<String> = arrayOf(),
    private val envVars: Map<String, String> = mapOf()
) : BaseObservableTask() {
    override val name: String = "CommandTask"

    protected var process: Process? = null
    protected var workingDirectory: File? = null
    private val cancellablePause = CancellablePause()

    override suspend fun startInCoroutine() {
        super.startInCoroutine()
        execute().collect {
            cancellablePause.addPausePoint()
            onReceiveOutput(it)
            GLog.d(name, "[start] $it")
        }
        onProcessEnd()
    }

    protected open suspend fun onReceiveOutput(line: String) {
        notifyProgress(line)
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    protected open fun execute(): Flow<String> {
        if (process?.isAlive == true) {
            GLog.w(name, "[execute] , CommandExecutor is now executing!")
            return emptyFlow()
        }
        return runCatching {
            // flow buffer size is 20MB
            channelFlow {
                async {
                    val builder = ProcessBuilder(*commands)
                        .directory(workingDirectory)
                        .redirectErrorStream(true)
                    builder.command().addAll(args)
                    builder.environment().putAll(envVars)
                    onPrepareProcess(builder)
                    val process = builder.start()
                    onProcessStart(this@channelFlow, process)
                }
            }.buffer(8 * 1024 * 1024 * 20, BufferOverflow.DROP_OLDEST)
        }.onFailure {
            GLog.e(name, "[execute]", it)
        }.getOrElse { emptyFlow() }
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    protected open suspend fun onProcessStart(producerScope: ProducerScope<String>, process: Process) {
        GLog.d(name, "[onProcessStart] set process $process")
        this.process = process
        producerScope.readOutput(process)
    }

    protected open fun onProcessEnd() {
        GLog.d(name, "[onProcessEnd] $process")
    }

    protected open fun onPrepareProcess(processBuilder: ProcessBuilder) {
        GLog.d(name, "[onProcessPrepared] $process")
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    private suspend fun ProducerScope<String>.readOutput(process: Process) {
        val scanner = Scanner(BufferedInputStream(process.inputStream))
        while (scanner.hasNextLine()) {
            send(scanner.nextLine())
        }
        GLog.d(name, "[readOutput] $process normally exit")
        close()
    }

    override fun stop() {
        super.stop()
        GLog.d(name, "[cancel] kill process $process")
        process?.destroyForcibly()
    }

    override fun pause() {
        super.pause()
        cancellablePause.pause()
    }

    override fun resume() {
        super.resume()
        cancellablePause.resume()
    }
}