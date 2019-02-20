package com.mylnikov

import java.nio.file.{Path, Paths}
import java.util.concurrent.{ExecutorService, LinkedBlockingQueue, ThreadPoolExecutor, TimeUnit}

import org.apache.commons.io.FileUtils
import org.scalatest.FunSuite

class SparkPerformanceTest extends FunSuite {

  test("Streaming should be faster") {

    //Init output and executor
    val outputPath = Paths.get("tmp")
    var EXECUTOR =
      new ThreadPoolExecutor(1, 1, 0L, TimeUnit.MILLISECONDS,
        new LinkedBlockingQueue[Runnable]())

    val resultOfBatch = runJobAndGetOutputSize(outputPath, EXECUTOR, new KafkaPollJob)

    //Wait until there will be some messages in queue
    Thread.sleep(10000)

    val resultOfStreaming = runJobAndGetOutputSize(outputPath, EXECUTOR, new SparkStreamingJob)

    assert(resultOfStreaming > resultOfBatch)
  }

  def runJobAndGetOutputSize(outputPath: Path, executor: ExecutorService, job: ClosableRunnable): Long = {
    executor.execute(job)
    Thread.sleep(30000)
    val sizeOfDirectory = FileUtils.sizeOfDirectory(outputPath.toFile)
    job.close()

    //Wait 1s for successful Thread finishing
    Thread.sleep(1000)
    while(outputPath.toFile.list().length != 0) {
      try {
        FileUtils.cleanDirectory(outputPath.toFile)
      } catch {
        case e: Exception => println("Unable to clean directory. Will try again.")
      }
      Thread.sleep(500)
    }
    sizeOfDirectory
  }

  class SparkStreamingJob extends ClosableRunnable {
    override def run(): Unit = {
      val args = new Array[String](2)
      args(0) = "localhost:9092"
      args(1) = "tmp"
      SparkStreaming.main(args)
    }

    override def close(): Unit = {
      SparkStreaming.ssc.stop(stopSparkContext = true, stopGracefully = true)
    }
  }

  class KafkaPollJob extends ClosableRunnable {

    override def run(): Unit = {
      val args = new Array[String](3)
      args(0) = "localhost:9092"
      args(1) = "tmp"
      args(2) = "kafkaBatch"
      KafkaBatching.main(args)
    }

    override def close(): Unit = {
      KafkaBatching.stopped = true
    }
  }

  trait ClosableRunnable extends Runnable {
    def close()
  }

}


