package ru.hse.openGL.objects;

/**
 * OpenGL color attachment storage class.
 */
public abstract class Attachment {
    // ID of the buffer to use.
    private int bufferId;
    private boolean isDepthAttachment = false;

    /**
     * Method for the attachmentType creation by initializing storage,
     * and attachmentType to the FBO.
     *
     * @param attachmentType type of the attachment
     * @param width          width of the FBO
     * @param height         height of the FBO
     * @param samplesAmount  number of samples that the FBO uses
     */
    public abstract void init(int attachmentType,
                              int width, int height,
                              int samplesAmount);

    /**
     * Method for the attachment deletion.
     */
    public abstract void delete();

    /**
     * Getter of the buffer ID.
     *
     * @return ID of the FBO
     */
    public int getBufferId() {
        return bufferId;
    }

    /**
     * Setter of the buffer ID.
     *
     * @param id ID of the FBO
     */
    public void setBufferId(int id) {
        this.bufferId = id;
    }

    /**
     * Method for changing the type of the attachment to a depth one.
     */
    public void setAsDepthAttachment() {
        this.isDepthAttachment = true;
    }

    /**
     * Method for getting, if the attachment is a depth one.
     *
     * @return true if the attachment is a depth one and false otherwise
     */
    public boolean isDepthAttachment() {
        return isDepthAttachment;
    }
}
