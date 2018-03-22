package com.alibaba.dubbo.common.logger.slf4j;

import com.alibaba.dubbo.common.logger.Logger;
import com.alibaba.dubbo.common.logger.support.FailsafeLogger;
import org.slf4j.spi.LocationAwareLogger;

import java.io.Serializable;

public class Slf4jLogger implements Logger, Serializable {

    private static final long serialVersionUID = 1L;
    private static String FQCN = FailsafeLogger.class.getName();
    private final org.slf4j.Logger logger;
    private LocationAwareLogger locationAwareLogger;

    private boolean lalEnable;

    public Slf4jLogger(org.slf4j.Logger logger) {
        this.logger = logger;
        if ((logger instanceof LocationAwareLogger)) {
            this.locationAwareLogger = (LocationAwareLogger) logger;
            lalEnable = true;
        }
    }

    public void trace(String msg) {
        if (lalEnable) {
            locationAwareLogger.log(null, FQCN, LocationAwareLogger.TRACE_INT, msg, null, null);
        } else {
            logger.trace(msg);
        }
    }

    public void trace(Throwable e) {
        if (lalEnable) {
            locationAwareLogger.log(null, FQCN, LocationAwareLogger.TRACE_INT, e.getMessage(), null, e);
        } else {
            logger.trace(e.getMessage(), e);
        }
    }

    public void trace(String msg, Throwable e) {
        if (lalEnable) {
            locationAwareLogger.log(null, FQCN, LocationAwareLogger.TRACE_INT, msg, null, e);
        } else {
            logger.trace(e.getMessage(), e);
        }
    }

    public void debug(String msg) {
        if (lalEnable) {
            locationAwareLogger.log(null, FQCN, LocationAwareLogger.DEBUG_INT, msg, null, null);
        } else {
            logger.trace(msg);
        }
    }

    public void debug(Throwable e) {
        if (lalEnable) {
            locationAwareLogger.log(null, FQCN, LocationAwareLogger.DEBUG_INT, e.getMessage(), null, e);
        } else {
            logger.trace(e.getMessage(), e);
        }
    }

    public void debug(String msg, Throwable e) {
        if (lalEnable) {
            locationAwareLogger.log(null, FQCN, LocationAwareLogger.DEBUG_INT, msg, null, e);
        } else {
            logger.trace(e.getMessage(), e);
        }
    }

    public void info(String msg) {
        if (lalEnable) {
            locationAwareLogger.log(null, FQCN, LocationAwareLogger.INFO_INT, msg, null, null);
        } else {
            logger.trace(msg);
        }
    }

    public void info(Throwable e) {
        if (lalEnable) {
            locationAwareLogger.log(null, FQCN, LocationAwareLogger.INFO_INT, e.getMessage(), null, e);
        } else {
            logger.trace(e.getMessage(), e);
        }
    }

    public void info(String msg, Throwable e) {
        if (lalEnable) {
            locationAwareLogger.log(null, FQCN, LocationAwareLogger.INFO_INT, msg, null, e);
        } else {
            logger.trace(e.getMessage(), e);
        }
    }

    public void warn(String msg) {
        if (lalEnable) {
            locationAwareLogger.log(null, FQCN, LocationAwareLogger.WARN_INT, msg, null, null);
        } else {
            logger.trace(msg);
        }
    }

    public void warn(Throwable e) {
        if (lalEnable) {
            locationAwareLogger.log(null, FQCN, LocationAwareLogger.WARN_INT, e.getMessage(), null, e);
        } else {
            logger.trace(e.getMessage(), e);
        }
    }

    public void warn(String msg, Throwable e) {
        if (lalEnable) {
            locationAwareLogger.log(null, FQCN, LocationAwareLogger.WARN_INT, msg, null, e);
        } else {
            logger.trace(e.getMessage(), e);
        }
    }

    public void error(String msg) {
        if (lalEnable) {
            locationAwareLogger.log(null, FQCN, LocationAwareLogger.ERROR_INT, msg, null, null);
        } else {
            logger.trace(msg);
        }
    }

    public void error(Throwable e) {
        if (lalEnable) {
            locationAwareLogger.log(null, FQCN, LocationAwareLogger.ERROR_INT, e.getMessage(), null, e);
        } else {
            logger.trace(e.getMessage(), e);
        }
    }

    public void error(String msg, Throwable e) {
        if (lalEnable) {
            locationAwareLogger.log(null, FQCN, LocationAwareLogger.ERROR_INT, msg, null, e);
        } else {
            logger.trace(e.getMessage(), e);
        }
    }

    public boolean isTraceEnabled() {
        return logger.isTraceEnabled();
    }

    public boolean isDebugEnabled() {
        return logger.isDebugEnabled();
    }

    public boolean isInfoEnabled() {
        return logger.isInfoEnabled();
    }

    public boolean isWarnEnabled() {
        return logger.isWarnEnabled();
    }

    public boolean isErrorEnabled() {
        return logger.isErrorEnabled();
    }

}
