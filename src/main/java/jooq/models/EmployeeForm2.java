package jooq.models;

public class EmployeeForm2 {

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

    public String getFormParent() {
        return formParent;
    }

    public void setFormParent(String formParent) {
        this.formParent = formParent;
    }

    private Integer formId;
    private String formName;
    private String formParent;

}