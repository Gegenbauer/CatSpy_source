package me.gegenbauer.catspy.log.datasource

import FileLogSourceFactory
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.emptyFlow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onCompletion
import kotlinx.coroutines.flow.onStart
import me.gegenbauer.catspy.concurrency.GIO
import me.gegenbauer.catspy.log.parse.LogParser
import java.io.File

class FileLogProducer(
    private val logPath: String,
    private val logParser: LogParser,
    override val dispatcher: CoroutineDispatcher = Dispatchers.GIO
) : BaseLogProducer() {

    override val tempFile: File = File(logPath)

    override fun start(): Flow<Result<LogItem>> {
        if (logPath.isBlank() || tempFile.exists().not()) {
            return emptyFlow()
        }
        return FileLogSourceFactory().createSource(tempFile).read().map { item ->
            suspender.checkSuspend()
            val num = logNum.getAndIncrement()
            Result.success(LogItem(num, logParser.parse(item.getPart(1))))
        }.onCompletion {
            moveToState(LogProducer.State.Complete)
        }.onStart {
            moveToState(LogProducer.State.intermediateRunning())
        }.flowOn(dispatcher)
    }
}