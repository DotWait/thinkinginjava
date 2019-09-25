package com.dotwait.innerclass;

public class Parcell {
    static class Contents{
        private int i = 11;
        public int value(){
            return 1;
        }
    }

    class Destination{
        private String label;

        public Destination(String label) {
            this.label = label;
        }

        String readLabel(){
            return label;
        }
    }

    public void ship(String dest){
        Contents c = new Contents();
        Destination d = new Destination(dest);
        System.out.println(d.readLabel());
    }

    public static void main(String[] args){
        Parcell p = new Parcell();
        p.ship("Tasmania");
    }
}
