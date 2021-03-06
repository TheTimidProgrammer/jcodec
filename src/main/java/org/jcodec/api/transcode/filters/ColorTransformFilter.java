package org.jcodec.api.transcode.filters;

import org.jcodec.common.logging.Logger;
import org.jcodec.common.model.ColorSpace;
import org.jcodec.common.model.Picture8Bit;
import org.jcodec.api.transcode.Filter;
import org.jcodec.api.transcode.PixelStore;
import org.jcodec.api.transcode.PixelStore.LoanerPicture;
import org.jcodec.scale.ColorUtil;
import org.jcodec.scale.Transform8Bit;

/**
 * Color transform filter.
 * 
 * @author Stanislav Vitvitskyy
 */
public class ColorTransformFilter implements Filter {
    private Transform8Bit transform;
    private ColorSpace encoderColor;

    public ColorTransformFilter(ColorSpace encoderColor) {
        this.encoderColor = encoderColor;
    }

    @Override
    public LoanerPicture filter(Picture8Bit picture, PixelStore store) {
        if (transform == null) {
            transform = ColorUtil.getTransform8Bit(picture.getColor(), encoderColor);
            Logger.debug("Creating transform: " + transform);
        }
        LoanerPicture outFrame = store.getPicture(picture.getWidth(), picture.getHeight(), encoderColor);
        outFrame.getPicture().setCrop(picture.getCrop());
        transform.transform(picture, outFrame.getPicture());
        return outFrame;
    }

    @Override
    public ColorSpace getInputColor() {
        // Any color space
        return null;
    }

    @Override
    public ColorSpace getOutputColor() {
        return encoderColor;
    }
}