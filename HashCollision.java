package hashCollision;


import java.util.HashMap;

class HashCollision {
    public static void main(String[] args) {

        HashMap map = new HashMap();
        HashCollisionModel p1 = new HashCollisionModel(1, "ABC");
        HashCollisionModel p2 = new HashCollisionModel(2, "DEF");
        HashCollisionModel p3 = new HashCollisionModel(1, "XYZ");
        HashCollisionModel p4 = new HashCollisionModel(1, "PQR");
        HashCollisionModel p5 = new HashCollisionModel(1, "PQR");
        System.out.println("Adding Entries ....");
        map.put(p1, "ONE");
        map.put(p2, "TWO");
        map.put(p3, "THREE");
        map.put(p4, "FOUR");
        map.put(p5, "FIVE");
        System.out.println("\nComplete Map entries \n" + map);
        System.out.println("\nAccessing non-collided key");
        System.out.println("Value = " + map.get(p2));
        System.out.println("\nAccessing collided key");
        System.out.println("Value = " + map.get(p1));
    }
}

