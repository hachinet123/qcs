package com.tre.centralkitchen.service.job;

import com.tre.centralkitchen.common.constant.SysConstants;
import com.tre.jdevtemplateboot.exception.global.SysBusinessException;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Comparator;
import java.util.stream.Stream;

/**
 * @author 10225441
 */
@Slf4j
public class AutoCleanTmpFolder {
    @Value(value = "${ck-system-business.tmp-file-path:/tmp}")
    private String path;

    @SneakyThrows(value = SysBusinessException.class)
    @Scheduled(cron = "${spring.cron.auto-clean-tmp-folder:0 0 */3 * *}")
    public void run() throws IOException {
        log.info(SysConstants.SCHEDULED_MSG_CLEAN_FOLDER_ST, path);
        try (Stream<Path> stream = Files.walk(Paths.get(path))) {
            stream.skip(1).sorted(Comparator.reverseOrder()).map(Path::toFile).forEach(file -> {
                try {
                    log.info(SysConstants.FILE_DELETE_MSG, file.getName(),
                            Files.deleteIfExists(file.toPath()) ? SysConstants.MSG_SUCCESS : SysConstants.MSG_FAILURE);
                } catch (IOException e) {
                    log.error(SysConstants.FILE_DELETE_MSG, file.getName(), SysConstants.MSG_FAILURE);
                    throw new SysBusinessException(e.getMessage());
                }
            });
        }
        log.info(SysConstants.SCHEDULED_MSG_CLEAN_FOLDER_ED, path);
    }
}
