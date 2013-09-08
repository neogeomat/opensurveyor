package devedroid.opensurveyor;

import java.util.ArrayList;
import java.util.List;

import org.osmdroid.api.IGeoPoint;
import org.osmdroid.views.MapView;
import org.osmdroid.views.overlay.Overlay;

import devedroid.opensurveyor.data.Drawing;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Point;
import android.graphics.Paint.Style;
import android.view.MotionEvent;

class DrawingsOverlay extends Overlay {
	// List<List> paths
	private List<List<IGeoPoint>> paths = new ArrayList<List<IGeoPoint>>();
	private Paint paint;

	public DrawingsOverlay(Context ctx) {
		super(ctx);
		paint = new Paint();
		paint.setColor(Color.BLACK);
		paint.setStrokeWidth(4);
		paint.setStyle(Style.STROKE);
	}

	@Override
	protected void draw(Canvas arg0, MapView map, boolean arg2) {
		Point pt = new Point();
		for (List<IGeoPoint> path : paths) {
			Path pth = new Path();
			boolean first = true;
			for (IGeoPoint gpt : path) {
				pt = map.getProjection().toMapPixels(gpt, pt);
				if (first)
					pth.moveTo(pt.x, pt.y);
				else
					pth.lineTo(pt.x, pt.y);
				first = false;
			}
			arg0.drawPath(pth, paint);
		}
	}
	
	public void addDrawing(Drawing dr) {
		paths.addAll(dr.getData() );
	}

}