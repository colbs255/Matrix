public class Vectrix extends Matrix {

    public Vectrix(double... x) {
        super(x);
    }

    public Vectrix(Matrix x) {
        super(x.getArray()[0]);
    }

    public double getElement(int pointer) {
        return super.getElement(0, pointer);
    }

    public double dot(Vectrix b) {
        double[][] x = (this.multiply(b.transpose())).getArray();
        return x[0][0];

    }

    public double getMagnitude() {
        return Math.sqrt(this.dot(this));
    }

    public double getAngleWith(Vectrix b) {
        double value = this.dot(b) / (this.getMagnitude() * b.getMagnitude());
        return Math.acos(value) * (180/Math.PI);
    }

    public Vectrix projectOn(Vectrix b) {
        double scalar = (this.dot(b)) / (b.dot(b));
        // System.out.println("Scalar: " + scalar);
        return new Vectrix((b.scale(scalar)).getArray()[0]);
    }

    public Vectrix getUnitVector() {
        return new Vectrix(scale(1/getMagnitude()).getArray()[0]);
    }

}