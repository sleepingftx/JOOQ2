package jooq.models;

public class Orgs {

    private Integer id;
    private String name;
    private Integer empls;

    public Integer getEmpls() {
        return empls;
    }

    public void setEmpls(Integer empls) {
        this.empls = empls;
    }

    public String getP_name() {
        return p_name;
    }

    public void setP_name(String p_name) {
        this.p_name = p_name;
    }

    public Integer getP_id() {
        return p_id;
    }

    public void setP_id(Integer p_id) {
        this.p_id = p_id;
    }

    private String p_name;
    private Integer p_id;

    public Orgs(){

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

    public Orgs(Integer id, String name, String p_name, Integer p_id, Integer empls){
        this.id =id;
        this.name=name;
        this.p_name=p_name;
        this.p_id=p_id;
        this.empls=empls;
    }

}
