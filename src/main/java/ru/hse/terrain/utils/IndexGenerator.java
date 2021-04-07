package ru.hse.terrain.utils;

/**
 * Class for indices generation.
 */
public class IndexGenerator {
    /**
     * Method for index buffer generation.
     *
     * @param verticesAmount amount of vertices
     * @return array of indices
     */
    public static int[] generateIndexBuffer(int verticesAmount) {
        int[] indices = new int[(verticesAmount - 1) *
                (verticesAmount - 1) * 6];

        int rowLength = (verticesAmount - 1) * 2;

        int pointer = storeUpperLines(indices, rowLength, verticesAmount);
        pointer = storePenultimateLine(indices, pointer, rowLength, verticesAmount);
        storeLastLine(indices, pointer, rowLength, verticesAmount);

        return indices;
    }

    /**
     * Method for upper lines storing.
     *
     * @param indices        indices used
     * @param rowLength      length of the row
     * @param verticesAmount amount of vertices
     * @return pointer
     */
    private static int storeUpperLines(int[] indices, int rowLength, int verticesAmount) {
        int pointer = 0;

        for (int row = 0; row < verticesAmount - 3; row++) {
            for (int column = 0; column < verticesAmount - 1; column++) {
                int topLeft = (row * rowLength) + (column * 2);
                int topRight = topLeft + 1;
                int bottomLeft = topLeft + rowLength;
                int bottomRight = bottomLeft + 1;

                pointer = storeQuad(topLeft, topRight,
                        bottomLeft, bottomRight,
                        indices, pointer,
                        column % 2 != row % 2);
            }
        }

        return pointer;
    }

    /**
     * Method for penultimate line storing.
     *
     * @param indices        indices used
     * @param pointer        pointer used
     * @param rowLength      length of the row
     * @param verticesAmount amount of vertices
     * @return pointer
     */
    private static int storePenultimateLine(int[] indices, int pointer,
                                            int rowLength, int verticesAmount) {
        int row = verticesAmount - 3;

        for (int column = 0; column < verticesAmount - 1; column++) {
            int topLeft = (row * rowLength) + (column * 2);
            int topRight = topLeft + 1;
            int bottomLeft = (topLeft + rowLength) - column;
            int bottomRight = bottomLeft + 1;

            pointer = storeQuad(topLeft, topRight,
                    bottomLeft, bottomRight,
                    indices, pointer,
                    column % 2 != row % 2);
        }

        return pointer;
    }

    /**
     * Method for last line storing.
     *
     * @param indices        indices used
     * @param pointer        pointer used
     * @param rowLength      length of the row
     * @param verticesAmount amount of vertices
     */
    private static void storeLastLine(int[] indices, int pointer,
                                      int rowLength, int verticesAmount) {
        int row = verticesAmount - 2;

        for (int column = 0; column < verticesAmount - 1; column++) {
            int topLeft = (row * rowLength) + column;
            int topRight = topLeft + 1;
            int bottomLeft = (topLeft + verticesAmount);
            int bottomRight = bottomLeft + 1;

            pointer = storeLastRowQuad(topLeft, topRight,
                    bottomLeft, bottomRight,
                    indices, pointer,
                    column % 2 != row % 2);
        }
    }

    /**
     * Method for quad storing.
     *
     * @param topLeft       top left index
     * @param topRight      top right index
     * @param bottomLeft    bottom left index
     * @param bottomRight   bottom right index
     * @param indices       indices used
     * @param pointer       pointer used
     * @param isRightHanded is right handed or not
     * @return pointer
     */
    private static int storeQuad(int topLeft, int topRight,
                                 int bottomLeft, int bottomRight,
                                 int[] indices, int pointer,
                                 boolean isRightHanded) {
        pointer = storeLeftTriangle(topLeft, topRight,
                bottomLeft, bottomRight,
                indices, pointer,
                isRightHanded);

        indices[pointer++] = topRight;
        indices[pointer++] = isRightHanded ? topLeft : bottomLeft;
        indices[pointer++] = bottomRight;

        return pointer;
    }

    /**
     * Method for last row quad storing.
     *
     * @param topLeft       top left index
     * @param topRight      top right index
     * @param bottomLeft    bottom left index
     * @param bottomRight   bottom right index
     * @param indices       indices used
     * @param pointer       pointer used
     * @param isRightHanded is right handed or not
     * @return pointer
     */
    private static int storeLastRowQuad(int topLeft, int topRight,
                                        int bottomLeft, int bottomRight,
                                        int[] indices, int pointer,
                                        boolean isRightHanded) {
        pointer = storeLeftTriangle(topLeft, topRight,
                bottomLeft, bottomRight,
                indices, pointer,
                isRightHanded);

        indices[pointer++] = bottomRight;
        indices[pointer++] = topRight;
        indices[pointer++] = isRightHanded ? topLeft : bottomLeft;

        return pointer;
    }

    /**
     * Method for last row quad storing.
     *
     * @param topLeft       top left index
     * @param topRight      top right index
     * @param bottomLeft    bottom left index
     * @param bottomRight   bottom right index
     * @param indices       indices used
     * @param pointer       pointer used
     * @param isRightHanded is right handed or not
     * @return pointer
     */
    private static int storeLeftTriangle(int topLeft, int topRight,
                                         int bottomLeft, int bottomRight,
                                         int[] indices, int pointer,
                                         boolean isRightHanded) {
        indices[pointer++] = topLeft;
        indices[pointer++] = bottomLeft;
        indices[pointer++] = isRightHanded ? bottomRight : topRight;

        return pointer;
    }
}
