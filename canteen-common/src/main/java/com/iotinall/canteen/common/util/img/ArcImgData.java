package com.iotinall.canteen.common.util.img;

import lombok.Data;

import java.awt.image.BufferedImage;

/**
 * @author bingo
 * @date 12/19/2019 20:01
 */
@Data
public class ArcImgData {
    private String featureData;
    private ImgMeta imgMeta;
    private BufferedImage img; //
}
