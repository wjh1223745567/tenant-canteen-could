package com.iotinall.canteen.common.constant;

import lombok.extern.slf4j.Slf4j;

import java.io.File;

@Slf4j
public class FileConstant {

    public final static File tmpDir = new File("tmpfile");

    static {
        if(!tmpDir.exists()){
            tmpDir.mkdirs();
        }
    }

}
