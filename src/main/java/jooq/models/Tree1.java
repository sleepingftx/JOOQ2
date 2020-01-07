package jooq.models;

public class Tree1 {

    private Integer id;
    private String name;
    private boolean child;

    public int[] getNodes() {
        return nodes;
    }

    public void setNodes(int[] nodes) {
        this.nodes = nodes;
    }

    private int[] nodes;
    public Tree1(){

    }

    public boolean getChild() {
        return child;
    }

    public void setChild(boolean child) {
        this.child = child;
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

    public Tree1(Integer id, String name, boolean child,int [] nodes){
        this.id =id;
        this.child = child;
        this.name=name;
        this.nodes=nodes;

    }

}
