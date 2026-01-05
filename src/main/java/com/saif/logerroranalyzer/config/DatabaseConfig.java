package com.saif.logerroranalyzer.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.boot.CommandLineRunner;
import com.saif.logerroranalyzer.entity.ErrorCode;
import com.saif.logerroranalyzer.repository.ErrorCodeRepository;
import com.saif.logerroranalyzer.enums.ErrorType;
import com.saif.logerroranalyzer.enums.ErrorSeverity;
import org.springframework.context.annotation.Configuration;

@Configuration
public class DatabaseConfig {

    @Bean
    CommandLineRunner initDatabase(ErrorCodeRepository repository) {
        return args -> {
            if (repository.count() == 0) {
                // Initialize with predefined error codes
                repository.save(new ErrorCode(
                        "SIGNAL_6", ErrorType.SYSTEM_ERROR,
                        "Application terminated due to SIGABRT signal (Signal 6)",
                        "Check for memory leaks, validate input parameters, review recent code changes, analyze core dump if available",
                        ErrorSeverity.CRITICAL, "signal 6,sigabrt,abort,crash"
                ));

                repository.save(new ErrorCode(
                        "DEVICE_NOT_OPEN", ErrorType.HARDWARE_ERROR,
                        "Serial device is not properly opened or connected",
                        "Verify device connection, check USB cables, restart device drivers, check device permissions",
                        ErrorSeverity.HIGH, "device not open,serial port,usb,connection"
                ));

                repository.save(new ErrorCode(
                        "CONNECTION_FAILED", ErrorType.NETWORK_ERROR,
                        "Failed to establish connection with external device or service",
                        "Check network connectivity, verify device IP address, restart network services, check firewall settings",
                        ErrorSeverity.HIGH, "connection failed,not connected,network,tcp,socket"
                ));

                repository.save(new ErrorCode(
                        "OPENTYPE_MISSING", ErrorType.CONFIGURATION_ERROR,
                        "OpenType font support is missing for the specified font",
                        "Install required font packages, use alternative fonts, update system font cache",
                        ErrorSeverity.MEDIUM, "opentype,font,missing,ubuntu"
                ));

                repository.save(new ErrorCode(
                        "SIGNAL_NOT_FOUND", ErrorType.SYSTEM_ERROR,
                        "Qt signal/slot connection failed - signal does not exist",
                        "Update Qt version, check signal/slot compatibility, review Qt documentation, verify method signatures",
                        ErrorSeverity.MEDIUM, "no such signal,qt,signal,slot"
                ));

                repository.save(new ErrorCode(
                        "LASER_CONNECTION", ErrorType.HARDWARE_ERROR,
                        "Laser device connection failure",
                        "Check laser power supply, verify USB connection, restart laser controller, check device drivers",
                        ErrorSeverity.CRITICAL, "laser,not connected,hardware"
                ));

                repository.save(new ErrorCode(
                        "SERIAL_PORT_ERROR", ErrorType.HARDWARE_ERROR,
                        "Serial port communication error",
                        "Check serial cable connections, verify port permissions, restart serial service, check baud rate settings",
                        ErrorSeverity.HIGH, "serial,port,communication,usb,tty"
                ));

                repository.save(new ErrorCode(
                        "RMQ_ERROR", ErrorType.NETWORK_ERROR,
                        "RabbitMQ messaging system error",
                        "Check RabbitMQ server status, verify connection parameters, restart messaging service, check queue permissions",
                        ErrorSeverity.HIGH, "rmq,rabbitmq,message,queue"
                ));

                repository.save(new ErrorCode(
                        "PLC_CONNECTION", ErrorType.HARDWARE_ERROR,
                        "PLC (Programmable Logic Controller) connection error",
                        "Verify PLC network connection, check IP address configuration, restart PLC communication module",
                        ErrorSeverity.HIGH, "plc,controller,automation,industrial"
                ));

                repository.save(new ErrorCode(
                        "SOCKET_ERROR", ErrorType.NETWORK_ERROR,
                        "TCP/UDP socket communication error",
                        "Check network connectivity, verify port availability, check firewall rules, restart network stack",
                        ErrorSeverity.MEDIUM, "socket,tcp,udp,network,port"
                ));
            }
        };
    }
}
