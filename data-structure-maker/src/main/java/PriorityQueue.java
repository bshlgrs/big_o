//public class PriorityQueue  {
//    String[] fields = {"priority", "id"};
//    // ideally I'd be able to not specify those two bools
//    MagicMultiset stuff = new MagicMultiset(fields, true, true);
//
//    int getIdOfCheapest() {
//        return stuff.orderDescendingBy(x -> x.priority).first.id;
//    }
//
//    int insertItem(int priority, int id) {
//        stuff.insert(priority, id);
//    }
//
//    int popCheapest() {
//        int cheapest = getIdOfCheapest();
//        stuff.remove(cheapest);
//        return cheapest;
//    }
//}
