package com.iotinall.canteen.utils;

import lombok.extern.slf4j.Slf4j;

import java.util.List;

@Slf4j
public class SimilarityUtil {
    public static Boolean cosine(List<Double> target, List<Double> mbtarget) {
       Double simVal = SimilarityUtil.getVectorValue(target, mbtarget);
        //大于0.8认为是同一照片
        return simVal >= 0.8;
    }

    public static Double getVectorValue(List<Double> va, List<Double> vb){
            if (va.size() > vb.size()) {
                int temp = va.size() - vb.size();
                for (int i = 0; i < temp; i++) {
                    vb.add(0.);
                }
            } else if (va.size() < vb.size()) {
                int temp = vb.size() - va.size();
                for (int i = 0; i < temp; i++) {
                    va.add(0.);
                }
            }

            int size = va.size();
            double simVal = 0;

            double num = 0;
            double den = 1;
            double powa_sum = 0;
            double powb_sum = 0;
            for (int i = 0; i < size; i++) {
                double a = va.get(i);
                double b = vb.get(i);

                num = num + a * b;
                powa_sum = powa_sum + Math.pow(a, 2);
                powb_sum = powb_sum + Math.pow(b, 2);
            }
            double sqrta = Math.sqrt(powa_sum);
            double sqrtb = Math.sqrt(powb_sum);
            den = sqrta * sqrtb;

            simVal = num / den;
            return simVal;
    }
}
