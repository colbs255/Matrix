public class Matrix {

    public class InvalidDimensionsException extends RuntimeException {

        private Object object;

        public InvalidDimensionsException(String message) {
            super(message);
        }

        public Object getObject() {
            return object;
        }

    }

    private final double[][] matrix;

    public Matrix(double[][] mat) {
        matrix = mat;
    }

    public Matrix(double[] mat) {
        matrix = new double[1][mat.length];
        for (int x = 0; x < mat.length; x++) {
            matrix[0][x] = mat[x];
        }
    }

    public Matrix multiply(Matrix mat) {
        double[][] other = mat.matrix;
        if (matrix[0].length != other.length) {
            throw new InvalidDimensionsException("Inner dimensions don't match for multiplication.");
        }
        double[][] product = new double[matrix.length][other[0].length];

        for (int x = 0; x < matrix.length; x++) {
            for (int y = 0; y < other[0].length ; y++) {
                    double sum = 0;
                for (int z = 0; z < matrix[0].length; z++) {
                    sum += (matrix[x][z] * other[z][y]);
                }
                product[x][y] = sum;
            }
        }
        return new Matrix(product);
    }

    public Matrix add(Matrix mat) {
        if (!((getNumberRows() == mat.getNumberRows())
            && (getNumberColumns() == mat.getNumberColumns()))) {
            throw new InvalidDimensionsException("Dimensions don't match for addition");
        }

        double[][] answer = new double[getNumberRows()][getNumberColumns()];
        for (int row = 0; row < getNumberRows(); row++) {
            for (int col = 0; col < getNumberColumns(); col++) {
                answer[row][col] = (this.getElement(row, col) + mat.getElement(row, col));
            }
        }
        return new Matrix(answer);
    }

    public Matrix subtract(Matrix mat) {
        return add(mat.scale(-1));
    }

    public Matrix scale(double scalar) {
        double[][] answer = this.getArray();

        for (int row = 0; row < getNumberRows(); row++) {
            for (int col = 0; col < getNumberColumns(); col++) {
                answer[row][col] *= scalar;
            }
        }

        return new Matrix(answer);
    }



    public double[][] getArray() {
        double[][] mat =  new double[matrix.length][matrix[0].length];
        for (int x = 0; x < matrix.length; x++) {
            for (int y = 0; y < matrix[0].length; y++) {
                mat[x][y] = matrix[x][y];
            }
        }
        return mat;
    }

    public int getNumberRows() {
        return matrix.length;
    }

    public int getNumberColumns() {
        return matrix[0].length;
    }

    public double getElement(int r, int c) {
        return matrix[r][c];
    }

    public Matrix pow(int pow) {
        if (!(isSquare())) {
            throw new InvalidDimensionsException("Dimensions invalid for powers of matrix");
        }
        if (pow == 0) {
            double[][] id = new double[getNumberRows()][getNumberRows()];
            for (int x = 0; x < id.length; x++) {
                id[x][x] = 1;
            }
            return new Matrix(id);
        }
        Matrix answer = this;
        for (int x = 0; x < pow - 1; x++) {
            answer = answer.multiply(this);
        }
        return answer;
    }

    public double[] getEigenValue() {
        if (!(isSquare())) {
            throw new InvalidDimensionsException("Not a square matrix");
        }
        double answer[] = new double[matrix.length];
        if (matrix.length == 2) {
            double b = -1 * (getElement(0,0) + getElement(1,1));
            System.out.println(b);
            double c = (getElement(0,0) * getElement(1,1)) - (getElement(0,1) * getElement(1,0));
            System.out.println(c);
            answer[0] = ((-1 * b) + Math.sqrt((b * b) - (4 * c)))/2;
            answer[1] = ((-1 * b) - Math.sqrt((b * b) - (4 * c)))/2;
            return answer;
        }

        if (matrix.length == 3) {

        }

        return null;
    }

    public boolean isEigenVector(Matrix mat) {
        if ((getNumberColumns() != mat.getNumberRows()) || (mat.getNumberColumns() != 1)) {
            return false;
        }
        Matrix product = multiply(mat);
        double c = product.getElement(0,0) / getElement(0,0);
        return (mat.scale(c).equals(product));
    }

    public boolean isEigenValue(double eig) {
        if (getNumberColumns() != getNumberRows()) {
            return false;
        }
        Matrix spec = this.subtract((getIdentity()).scale(eig));
        System.out.println(spec);
        return (spec.rank() != getNumberColumns());
    }


    public Matrix getIdentity() {
        double[][] id = new double[getNumberColumns()][getNumberColumns()];
        for (int x = 0; x < id.length; x++) {
            id[x][x] = 1;
        }
        return new Matrix(id);
    }


    public Matrix rref() {
        double[][] other = matrix;

        //main for loop for columns
        int pivotCount = 0;
        for (int column = 0;  column < other[0].length; column++) {
            //looks for nonzero terms
            boolean allZeroes = true;
            int potentialSwitchIndex = pivotCount;
            for (int rowCheck = pivotCount; rowCheck < other.length; rowCheck++) {
                if (other[rowCheck][column] != 0) {
                    allZeroes = false;
                    potentialSwitchIndex = rowCheck;
                }
            }
            //skips this columns if all zeroes
            if (allZeroes) {
                continue;
            }

            //switch section
            if (Math.abs(other[pivotCount][column]) < .00000000000000001) {
                double[] top = other[pivotCount];
                double[] switcher = other[potentialSwitchIndex];
                other[pivotCount] = switcher;
                other[potentialSwitchIndex] = top;
            }

            //actual row reduction
            for (int row = 0; row < other.length; row++) {
                if (row == pivotCount) {
                    continue;
                }
                double c = -1 * (other[row][column] / other[pivotCount][column]);
                for (int rowElement = column; rowElement < other[row].length; rowElement++) {
                    other[row][rowElement] += (c * other[pivotCount][rowElement]);
                }
            }

            //scalar operations
            double d = (1 / other[pivotCount][column]);
            for (int elem = pivotCount; elem < other[0].length; elem++) {
                other[pivotCount][elem] *= d;
            }
            pivotCount++;
        }
        return new Matrix(other);
    }

    public Matrix transpose() {
        double[][] other = new double[getNumberColumns()][getNumberRows()];
        for (int x = 0; x < getNumberColumns(); x++) {
            double[] temp = new double[getNumberRows()];
            for (int y = 0; y < getNumberRows(); y++) {
                temp[y] = matrix[y][x];
            }
            other[x] = temp;
        }
        return new Matrix(other);
    }

    public double determinant() {
        if (matrix.length == 2) {
            return (matrix[0][0] * matrix[1][1]) - (matrix[0][1] * matrix[1][0]);
        }
        int coeffecient = 1;
        double sum = 0;
        int ln = matrix.length;

        // every column of matrix
        for (int x = 0; x < ln; x++) {

                //makes subarray for recursion
                double[][] subArray = new double[ln - 1][ln - 1];
                for (int a = 1; a < ln; a++) {
                    int num = 0;
                    for (int b = 0; b < ln; b++) {
                        if (b == x) {
                            continue;
                        }
                        subArray[a - 1][num] = matrix[a][b];
                        num++;
                    }
                }
            Matrix mat = new Matrix(subArray);
            sum += ((coeffecient * matrix[0][x]) * mat.determinant());
            coeffecient *= -1;
        }
        return sum;
    }

    public Matrix invert() {
        if (!isInvertable()) {
            throw new InvalidDimensionsException("Matrix is not invertible");
        }
        double[][] id = getIdentity().getArray();

        double[][] augmented = ((augmentWith(new Matrix(id))).rref()).getArray();

        double[][] inverted = new double[getNumberRows()][getNumberRows()];

        for (int row = 0; row < inverted.length; row++) {
            for (int col = 0; col < inverted[0].length; col++) {
                inverted[row][col] = augmented[row][col + augmented.length];
            }
        }
        return new Matrix(inverted);
    }

    public int rank() {
        double[][] other = rref().getArray();
        int pivotCount = 0;
        for (int row = 0; row < other.length; row++) {
            boolean hasPivot = false;
            for (int col = 0; col <other[0].length; col++) {
                if (Math.abs(other[row][col]) > .00000000000000000000001 ) {
                    hasPivot = true;
                }
            }
            if (hasPivot) {
                pivotCount++;
            }
        }
        return pivotCount;
    }

    public Matrix googleMatrix() {
        if (!(isSquare())) {
            throw new InvalidDimensionsException("Matrix is not Square for google matrix");
        }
        double[][] kArray = new double[getNumberRows()][getNumberColumns()];
        for (int row = 0; row < getNumberRows(); row++) {
            for (int col = 0; col < getNumberColumns(); col++) {
                kArray[row][col] = (1.0 / getNumberColumns());
            }
        }

        Matrix kMatrix = new Matrix(kArray);
        System.out.println(kMatrix);
        double p = .85;
        System.out.println(this.scale(p));
        System.out.println(kMatrix.scale(1-p));
        return ((this.scale(p)).add(kMatrix.scale(1-p)));
    }

    public Matrix orthogonalBasis() {
        double[][] mT = transpose().getArray();
        Vectrix[] vects = new Vectrix[mT.length];
        for (int x = 0; x < mT.length; x++) {
            vects[x] = new Vectrix(mT[x]);
        }

        Vectrix[] bases = new Vectrix[this.rank()];

        for (int y = 0; y < vects.length; y++) {
            Vectrix ans = vects[y];
            for (int j = 0; j < y; j++) {
                ans = new Vectrix(ans.subtract((vects[y]).projectOn(bases[j])));
            }
            bases[y] = ans;
        }

        Matrix answer = bases[0].transpose();
        for (int k = 1; k < bases.length; k++) {
            answer = answer.augmentWith(bases[k].transpose());
        }
        return answer;
    }

    public int nullity() {
        return (getNumberColumns() - rank());
    }

    public boolean isOneToOne() {
        return (getNumberColumns() == rank());
    }

    public boolean isOnto() {
        return (getNumberRows() == rank());
    }


    public boolean isInvertable() {
        if (!(isSquare())) {
            return false;
        }
        if (getNumberRows() < 6) {
            return (determinant() > .00000000000000000001);
        }
        return (rank() == getNumberRows());
    }

    public boolean isSquare() {
        return (matrix[0].length == matrix.length);
    }

    public Matrix augmentWith(Matrix end) {
        if (matrix.length != end.getArray().length) {
            throw new InvalidDimensionsException("Dimensions aren't compatible for augmentation.");
        }
        int rows = matrix.length;
        int columns = matrix[0].length;
        double[][] other = end.getArray();
        int endColumns = other[0].length;
        double[][] augmentedMatrix = new double[rows][columns + endColumns];

        for (int row = 0; row < rows; row++) {
            for (int col = 0; col < columns + endColumns; col++) {
                if (!(col >= columns)) {
                    augmentedMatrix[row][col] = matrix[row][col];
                } else {
                    augmentedMatrix[row][col] = other[row][col - columns];
                }
            }
        }

        return new Matrix(augmentedMatrix);
    }

    public Vectrix toVectrix() {
        double[] ar = new double[matrix.length];
        for (int x = 0; x < matrix.length ; x++) {
            ar[x] = matrix[x][0];
        }
        return new Vectrix(ar);
    }

    @Override
    public String toString() {
        String s = "";
        for (int x = 0; x < matrix.length; x++) {
            for (int y = 0; y < matrix[x].length; y++) {
                s += ("[" + matrix[x][y] + "] ");
            }
            s += "\n";

        }
        return s;
    }

    @Override
    public boolean equals(Object other) {
        if (other == null) {
           return false;
        }
        if (this == other) {
            return true;
        }
        if (!(other instanceof Matrix)) {
            return false;
        }
        Matrix that = (Matrix) other;
        double[][] thisArray = this.getArray();
        double[][] otherArray = that.getArray();
        if ((this.getNumberColumns() == that.getNumberColumns())
            && (this.getNumberRows() == that.getNumberRows())) {

            for (int row = 0; row < getNumberRows(); row++) {
                for (int col = 0; col < getNumberColumns(); col++) {
                    if (Math.abs(thisArray[row][col] - otherArray[row][col])
                        > .000000000000000001) {
                        return false;
                    }
                }
            }
            return true;
        }
        return false;
    }

}