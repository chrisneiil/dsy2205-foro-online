package cl.duoc.dsy2205.forum.model;

import jakarta.persistence.*;

@Entity @Table(name="FORUM_CATEGORIES")
public class Category {
    @Id @GeneratedValue(strategy=GenerationType.IDENTITY) private Long id;
    @Column(nullable=false,unique=true,length=60) private String name;
    @Column(nullable=false,length=240) private String description;
    protected Category(){}
    public Long getId(){return id;} public String getName(){return name;} public String getDescription(){return description;}
}
