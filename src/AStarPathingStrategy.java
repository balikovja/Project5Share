import java.util.PriorityQueue;
import java.util.List;
import java.util.Collections;
import java.util.function.BiPredicate;
import java.util.stream.Stream;
import java.util.stream.Collectors;
import java.util.function.Function;
import java.util.Queue;
import java.util.function.Predicate;
import java.util.*;

class AStarPathingStrategy implements PathingStrategy{
    public List<Point> computePath(Point start, Point end, Predicate<Point> canPassThrough,
                                   BiPredicate<Point, Point> withinReach, Function<Point,
                                   Stream<Point>> potentialNeighbors) {

        Queue<Spot> oList = new PriorityQueue<>(Comparator.comparingInt(Spot::getF));
        Spot startN = new Spot(0, h(start, end), start, null);
        oList.add(startN);
        Map<Point, Spot> oMap = new HashMap<>();
        Map<Point, Spot> cMap = new HashMap<>();
        List<Point> computedPath = new ArrayList<>();
        Spot curr;
        while (!oList.isEmpty()) {
            curr = oList.remove();
            if (withinReach.test(curr.getPosition(), end)){
                computedPath = computedPath(computedPath, curr);
                computedPath.remove(0);
                return computedPath;
            }
            List<Point> adjacentSpots = potentialNeighbors.apply(curr.getPosition()).filter(canPassThrough)
                                        .filter(p -> !p.equals(start) && !p.equals(end)).collect(Collectors.toList());
            for (Point adjSpot : adjacentSpots) {
                if (!cMap.containsKey(adjSpot)) {

                    int tempG = curr.getG() + 1;
                    if(oMap.containsKey(adjSpot)) {
                        if(tempG<oMap.get(adjSpot).getG()){
                            Spot betterSpot = new Spot(tempG,h(adjSpot, end) + tempG, adjSpot, curr);
                            oList.add(betterSpot);
                            oList.remove(oMap.get(adjSpot));
                            oMap.replace(adjSpot, betterSpot);
                        }
                    }
                    else {
                        Spot aSpot = new Spot(curr.getG() + 1, curr.getG() + 1 + h(adjSpot, end), adjSpot, curr);
                        oList.add(aSpot);
                        oMap.put(adjSpot, aSpot);
                    }
                }
                oList.remove(curr);
                cMap.put(curr.getPosition(), curr);
            }
        }
        if(computedPath.size()>0)
        {
            computedPath.remove(0);
        }
        return computedPath;
    }

    public int h(Point current, Point goal){return Functions.distanceSquared(current, goal); }

    public List<Point> computedPath(List<Point> compPath, Spot winner)
    {
        compPath.add(winner.getPosition());
        if(winner.getPSpot() == null)
        {
            Collections.reverse(compPath);
            return compPath;
        }
        return computedPath(compPath, winner.getPSpot());

    }
}
