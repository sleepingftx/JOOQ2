package jooq.models;

public class Employeee {

    private Integer id;
    private String name;
    private String org_name;
    private String s_name;


    public String getS_name() {
        return s_name;
    }

    public void setS_name(String s_name) {
        this.s_name = s_name;
    }



    public String getOrg_name() {
        return org_name;
    }

    public void setOrg_name(String org_name) {
        this.org_name = org_name;
    }


    public Employeee(){

    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Employeee(Integer id, String name, String org_name,String s_name){
        this.id =id;
        this.name=name;
        this.org_name=org_name;
        this.s_name=s_name;
    }

}
