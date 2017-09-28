package com.vo;

import javax.persistence.*;
import java.util.Arrays;

/**
 * Created by admin on 2016/8/29.
 */
@Entity
@Table(name = "TEST_USER", schema = "DB2INST1", catalog = "")
public class TestDb2UserVO {
    private Integer id;
    private String name;
    private String birthday;
    private byte[] longcontent;

    @Id
    @Column(name = "ID")
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    @Basic
    @Column(name = "NAME")
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Basic
    @Column(name = "BIRTHDAY")
    public String getBirthday() {
        return birthday;
    }

    public void setBirthday(String birthday) {
        this.birthday = birthday;
    }

    @Basic
    @Column(name = "LONGCONTENT")
    public byte[] getLongcontent() {
        return longcontent;
    }

    public void setLongcontent(byte[] longcontent) {
        this.longcontent = longcontent;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        TestDb2UserVO that = (TestDb2UserVO) o;

        if (id != null ? !id.equals(that.id) : that.id != null) return false;
        if (name != null ? !name.equals(that.name) : that.name != null) return false;
        if (birthday != null ? !birthday.equals(that.birthday) : that.birthday != null) return false;
        if (!Arrays.equals(longcontent, that.longcontent)) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = id != null ? id.hashCode() : 0;
        result = 31 * result + (name != null ? name.hashCode() : 0);
        result = 31 * result + (birthday != null ? birthday.hashCode() : 0);
        result = 31 * result + Arrays.hashCode(longcontent);
        return result;
    }
}
