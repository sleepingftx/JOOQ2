package jooq.models;

public class EmployeeForm {

    public Integer getFormId() {
        return formId;
    }

    public void setFormId(Integer formId) {
        this.formId = formId;
    }

    public String getFormName() {
        return formName;
    }

    public void setFormName(String formName) {
        this.formName = formName;
    }

    public String getFormOrg() {
        return formOrg;
    }

    public void setFormOrg(String formOrg) {
        this.formOrg = formOrg;
    }

    public String getFormSV() {
        return formSV;
    }

    public void setFormSV(String formSV) {
        this.formSV = formSV;
    }

    private Integer formId;
    private String formName;
    private String formOrg;
    private String formSV;

}