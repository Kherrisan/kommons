package cn.kherrisan.kommons

import mu.KotlinLogging
import org.apache.logging.log4j.LogManager
import org.slf4j.LoggerFactory

fun main() {
    val logger = KotlinLogging.logger {}
    logger.trace("TRACE")
    logger.debug("DEBUG")
    logger.info("INFO")
    logger.warn("WARN")
    logger.error("ERROR")
}