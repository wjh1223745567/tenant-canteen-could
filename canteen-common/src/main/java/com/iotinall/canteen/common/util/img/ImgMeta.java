package com.iotinall.canteen.common.util.img;

import lombok.Data;

import java.awt.image.BufferedImage;

/**
 * @author bingo
 * @date 12/19/2019 21:16
 */
@Data
public class ImgMeta {
    private ImgType imgType;
    private Orientation orientation;
    private BufferedImage img;

    ImgMeta(){}
    // ordinary 千万不能用
    public enum Orientation {
        Normal(360),
        Rotate_90(90),
        Rotate_180(180),
        Rotate_270(270);
        private final int angle; // 角度
        Orientation(int angle) {
            this.angle = angle;
        }

        /**
         * 获取回归到正方向时，需要选转的角度
         * @return 顺时针旋转的角度
         */
        public int getRotate() {
            return 360 - angle;
        }
    }

    public enum ImgType {
        JPEG("JPEG", "jpg"), PNG("PNG", "png"),
        UnKnownType("JPEG", "jpg"); // 不支持的类型

        ImgType(String name, String extName) {
            this.name = name;
            this.extName = extName;
        }
        private final String name;
        private final String extName; // 扩展名
        public String getExtName() {
            return extName;
        }
        public String getName() {
            return name();
        }
    }
}
