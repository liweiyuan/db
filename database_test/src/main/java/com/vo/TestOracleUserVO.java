package com.vo;

import javax.persistence.*;

/**
 * Created by admin on 2016/8/28.
 */
@Entity
@Table(name = "TEST_USER", schema = "SYSTEM", catalog = "")
public class TestOracleUserVO {
    private Integer id;
    private String longcontent;
    private String name;
    private String birthday;

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
    @Id
    @Basic
    @Column(name = "ID")
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    @Basic
    @Column(name = "LONGCONTENT")
    public String getLongcontent() {
        return longcontent;
    }

    public void setLongcontent(String longcontent) {
        this.longcontent = longcontent;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        TestOracleUserVO that = (TestOracleUserVO) o;

        if (id != null ? !id.equals(that.id) : that.id != null) return false;
        if (longcontent != null ? !longcontent.equals(that.longcontent) : that.longcontent != null) return false;
        if (name != null ? !name.equals(that.name) : that.name != null) return false;
        if (birthday != null ? !birthday.equals(that.birthday) : that.birthday != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = id != null ? id.hashCode() : 0;
        result = 31 * result + (longcontent != null ? longcontent.hashCode() : 0);
        result = 31 * result + (name != null ? name.hashCode() : 0);
        result = 31 * result + (birthday != null ? birthday.hashCode() : 0);
        return result;
    }
}
