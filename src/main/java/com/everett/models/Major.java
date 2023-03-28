package com.everett.models;

import java.util.Objects;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import org.hibernate.search.annotations.Field;
import org.hibernate.search.annotations.FieldBridge;
import org.hibernate.search.bridge.builtin.LongBridge;

@Entity(name = "Majors")
@Table(name = "Majors", schema = "PUBLIC")
public class Major {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "majorId", nullable = false)
    private Long majorId;

    @Column(name = "majorName", nullable = false)
    private String majorName;

    @Column(name = "majorDescription", nullable = false)
    private String majorDescription;

    @Column(name = "followers", nullable = false)
    private Long followers;

    @OneToMany(mappedBy = "major", targetEntity = Post.class, fetch = FetchType.LAZY, orphanRemoval = true, cascade = CascadeType.REMOVE)
    private Set<Post> posts;

    public Major() {
    }

    public Major(String majorName, String majorDescription, Long followers) {
        this.majorName = majorName;
        this.majorDescription = majorDescription;
        this.followers = followers;
    }

    @FieldBridge(impl = LongBridge.class)
    @Field
    public Long getMajorId() {
        return majorId;
    }

    public void setMajorId(Long majorId) {
        this.majorId = majorId;
    }

    public String getMajorName() {
        return majorName;
    }

    public void setMajorName(String majorName) {
        this.majorName = majorName;
    }

    public String getMajorDescription() {
        return majorDescription;
    }

    public void setMajorDescription(String majorDescription) {
        this.majorDescription = majorDescription;
    }

    public Long getFollowers() {
        return followers;
    }

    public void setFollowers(Long followers) {
        this.followers = followers;
    }

    @Override
    public int hashCode() {
        return Objects.hash(majorName);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o == null || getClass() != o.getClass())
            return false;
        Major major = (Major) o;
        return Objects.equals(majorName, major.majorName);
    }

}
