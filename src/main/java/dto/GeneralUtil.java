package dto;

import exception.CustomException;

public class GeneralUtil {

    public static void nullThrow(Object obj) throws Exception{
        if(obj==null){
            throw new CustomException("空对象");
        }
    }
}
