public class Spot {
        private int g;
        private int f;
        private Spot pSpot;
        private Point position;

        public Spot (int g, int f, Point position, Spot pSpot){
            this.g = g;
            this.f = f;
            this.pSpot = pSpot;
            this.position = position;
        }

        public Point getPosition()
        {
            return position;
        }

        public Spot getPSpot()
        {
            return pSpot;
        }

        public int getG()
        {
            return g;
        }

        public int getF()
        {
            return f;
        }

    }

