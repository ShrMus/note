package cn.shrmus.annotation.main;

import cn.shrmus.annotation.vo.UserExcelVO;
import cn.shrmus.annotation.vo.UserVO;

import java.lang.reflect.Field;

/**
 * 写这个类的目的：
 * 在工作中做导入操作的时候
 * 项目中的代码大量复制原先的写法
 * 即导入的VO用ExcelVO
 * 这种ExcelVO中用到了@Index注解
 * 而传递给数据访问层框架的是原先蚂蚁加@Index注解的VO
 * 导致每个导入操作都要做一遍转换VO的操作，浪费了大量时间
 * 因此写一个工具类
 * @Author Shr
 * @CreateDate 2019/6/10 21:51
 * @Version 1.0
 */
public class Main {
    public static void main(String[] args) {
        UserExcelVO userExcelVO = new UserExcelVO("张三",25,"湖南长沙");
        UserVO userVO = convertVOByReflex(userExcelVO, UserVO.class);
        System.out.println(userVO);
        System.out.println(UserVO.class);
    }

    /**
     * 类型转换方法
     * @param sourceVO 源对象
     * @param targetVOClass 目标对象运行时类型
     * @param <Source> 泛型，源对象类型
     * @param <Target> 泛型，目标对象类型
     * @return targetVO 目标对象实例
     */
    private static <Source,Target> Target convertVOByReflex(Source sourceVO, Class<Target> targetVOClass) {
        Target targetVO = null;
        try {
            targetVO = targetVOClass.newInstance();
            Class sourceVOClass = sourceVO.getClass();
            // 获取源对象的所有字段
            Field[] sourceVOClassDeclaredFields = sourceVOClass.getDeclaredFields();
            for (Field sourceVOClassDeclaredField : sourceVOClassDeclaredFields) {
                sourceVOClassDeclaredField.setAccessible(true);
                // 获得源对象字段名
                String sourceVOClassDeclaredFieldValueName = sourceVOClassDeclaredField.getName();
                // 获取源对象字段值
                Object sourceVOClassDeclaredFieldValue = sourceVOClassDeclaredField.get(sourceVO);
                // 获取目标对象的所有字段
                Field[] targetVOClassDeclaredFields = targetVOClass.getDeclaredFields();
                for (Field targetVOClassDeclaredField : targetVOClassDeclaredFields) {
                    targetVOClassDeclaredField.setAccessible(true);
                    // 获得目标对象字段名
                    String targetVOClassDeclaredFieldValueName = targetVOClassDeclaredField.getName();
                    // 源对象字段名和目标对象字段名相等
                    if (sourceVOClassDeclaredFieldValueName.equals(targetVOClassDeclaredFieldValueName)) {
                        // 将源对象字段值赋给目标对象的字段
                        targetVOClassDeclaredField.set(targetVO,sourceVOClassDeclaredFieldValue);
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return targetVO;
    }
}
