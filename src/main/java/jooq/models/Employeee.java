package jooq.models;

public class Employeee {

    private Integer id;
    private String name;
    private String org_name;
    private String s_name;
    private Integer org_id;
    private Integer s_id;


    public Integer getS_id() {
        return s_id;
    }

    public void setS_id(Integer s_id) {
        this.s_id = s_id;
    }


    public Integer getOrg_id() {
        return org_id;
    }

    public void setOrg_id(Integer org_id) {
        this.org_id = org_id;
    }




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

    public Employeee(Integer id, String name, String org_name,String s_name,Integer org_id,Integer s_id){
        this.id =id;
        this.name=name;
        this.org_name=org_name;
        this.s_name=s_name;
        this.org_id=org_id;
        this.s_id=s_id;
    }

}
