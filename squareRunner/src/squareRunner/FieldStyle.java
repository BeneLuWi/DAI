package squareRunner;

import java.awt.Color;

import repast.simphony.visualizationOGL2D.DefaultStyleOGL2D;
import saf.v3d.scene.VSpatial;

public class FieldStyle extends DefaultStyleOGL2D {
	
	@Override
	public Color getColor(Object o) {
		Field field = (Field) o;
		switch(field.getType()) {
			case IMPASSABLE:
				return Color.BLACK;
			case SLIPPERY:
				return Color.YELLOW;
			case EXIT:
				return Color.GREEN;
			case TRAP:
				return Color.RED;
			default:
				return Color.WHITE;
		}
			
	}
	
	@Override
	public VSpatial getVSpatial(Object agent, VSpatial spatial) {
		if (spatial == null) {
		  spatial = shapeFactory.createRectangle(18, 18);
		}
		return spatial;
	}
	
	@Override
	public int getBorderSize(Object object) {
	    return 1;
	}
	
}
