package com.example.intelligentequipmentinspectionsystem;

public class Question {
    private String formId;
    private String equipmentId;
    private String questionId;
    private String groupAnswerId;
    private String questionTitle;
    private String normalOrDefective = "null";
    private String followUpAction = "";

    public Question() {
    }

    public String getGroupAnswerId() {
        return groupAnswerId;
    }

    public void setGroupAnswerId(String groupAnswerId) {
        this.groupAnswerId = groupAnswerId;
    }

    public String getFormId() {
        return formId;
    }

    public void setFormId(String formId) {
        this.formId = formId;
    }

    public String getEquipmentId() {
        return equipmentId;
    }

    public void setEquipmentId(String equipmentId) {
        this.equipmentId = equipmentId;
    }

    public String getQuestionId() {
        return questionId;
    }

    public void setQuestionId(String questionId) {
        this.questionId = questionId;
    }

    public String getQuestionTitle() {
        return questionTitle;
    }

    public void setQuestionTitle(String questionTitle) {
        this.questionTitle = questionTitle;
    }

    public String getNormalOrDefective() {
        return normalOrDefective;
    }

    public void setNormalOrDefective(String normalOrDefective) {
        this.normalOrDefective = normalOrDefective;
    }

    public String getFollowUpAction() {
        return followUpAction;
    }

    public void setFollowUpAction(String followUpAction) {
        this.followUpAction = followUpAction;
    }
}
