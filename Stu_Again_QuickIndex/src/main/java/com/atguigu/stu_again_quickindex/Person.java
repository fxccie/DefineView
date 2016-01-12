package com.atguigu.stu_again_quickindex;

/**
 * Created by Fx on 2016/1/7.
 */
public class Person implements Comparable<Person>{
    private String name;
    private String pinYin;

    public Person(String name) {
        this.name = name;
        this.pinYin = PinYinUtils.getPinYin(name);
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPinYin() {
        return pinYin;
    }

    public void setPinYin(String pinYin) {
        this.pinYin = pinYin;
    }

    @Override
    public String toString() {
        return "Person{" +
                "name='" + name + '\'' +
                ", pinYin='" + pinYin + '\'' +
                '}';
    }


    /**
     * 实现此方法，Person对象才能排序
     * @param another
     * @return
     */
    @Override
    public int compareTo(Person another) {
        return pinYin.compareTo(another.pinYin);
    }
}
