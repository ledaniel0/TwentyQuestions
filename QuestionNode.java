package Project5;

public class QuestionNode {
    public String value;
    public QuestionNode left;
    public QuestionNode right;

    public QuestionNode(String value) {
        this.value = value;
    }

    public String getValue() {
        return this.value;
    }

    public QuestionNode getLeft() {
        return this.left;
    }

    public QuestionNode getRight() {
        return this.right;
    }

    public void setLeft(QuestionNode yes) {
        this.left = yes;
    }

    public void setRight(QuestionNode no) {
        this.right = no;
    }

    public boolean isAnswer() {
        return !this.value.endsWith("?");
    }

}
