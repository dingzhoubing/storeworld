package com.storeworld.analyze;

import java.io.File;
import java.io.IOException;
import java.text.DecimalFormat;
import java.util.List;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.PaintEvent;
import org.eclipse.swt.events.PaintListener;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.ImageData;
import org.eclipse.swt.graphics.ImageLoader;
import org.eclipse.swt.widgets.Canvas;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartUtilities;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.labels.CategoryItemLabelGenerator;
import org.jfree.chart.labels.ItemLabelAnchor;
import org.jfree.chart.labels.ItemLabelPosition;
import org.jfree.chart.labels.StandardCategoryItemLabelGenerator;
import org.jfree.chart.plot.CategoryPlot;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.renderer.category.LineAndShapeRenderer;
import org.jfree.data.category.DefaultCategoryDataset;
import org.jfree.ui.TextAnchor;

import com.storeworld.analyze.AnalyzerUtils.KIND;
import com.storeworld.analyze.AnalyzerUtils.TYPE;
import com.storeworld.analyze.trendutils.TrendDataSet;
import com.storeworld.pojo.dto.AnalysticDTO;

/**
 * show the trend graph of the result
 * @author dingyuanxiong
 *
 */
public class TrendComposite extends Composite implements AnalyzerBase{
	
	private TrendDataSet args;
	private List<Object> ds;
	
	public TrendComposite(Composite parent, int style, TrendDataSet args, List<Object> ds) {
		super(parent, style);
		this.args = args;
		ds.clear();
		ds.addAll(ds);
				
		showComposite();
	}
	/**
	 * get the trend image and show it on the compiste
	 * @return
	 */
	private Image computeTrend(){
		Image image = null;
		//compute to get the trend image
		return image;
	}
	
//	private DefaultCategoryDataset createDataset(){
//		 
//	}
	private File ComputeImage(List<Object> ds){
		//for test
        JFreeChart mChart = null;
		DefaultCategoryDataset dataset = new DefaultCategoryDataset();
		
		//parse the dataset
		for(int i=0;i<ds.size();i++){
			AnalysticDTO item = (AnalysticDTO)ds.get(i);
			double f1 = Double.valueOf(item.getField1());
			String f2 = item.getField2();
			String f3 = item.getField3();
			String year = f3.substring(0, 4);
			String mon = f3.substring(4, 6);
			String x = "";
			if(args.getType().equals(TYPE.MONTH)){								
				String d = f3.substring(6, 8);
				x=year+"/"+mon+"/"+d;
			}else{
				x=year+"/"+mon;
			}
			dataset.setValue(f1, f2, x);
		}
		
//		dataset.setValue(200, "shipment", "13/4");
//		dataset.setValue(300, "shipment", "14/4");
//		dataset.setValue(250, "shipment", "15/4");
//		dataset.setValue(600, "shipment", "16/4");
//		dataset.setValue(300, "shipment", "17/4");
		
		mChart = ChartFactory.createLineChart("", "", "", dataset, PlotOrientation.VERTICAL, false, true, false);
		
		CategoryPlot cp = (CategoryPlot) mChart.getPlot();
		LineAndShapeRenderer xylineandshaperenderer = (LineAndShapeRenderer)cp.getRenderer();
	    xylineandshaperenderer.setBaseShapesVisible(true);
	    LineAndShapeRenderer xyitem = (LineAndShapeRenderer) cp.getRenderer();  
	    //show the detail value
	    xyitem.setBaseItemLabelsVisible(true);  
	    xyitem.setBasePositiveItemLabelPosition(new ItemLabelPosition(ItemLabelAnchor.OUTSIDE12, TextAnchor.BASELINE_LEFT));
	    CategoryItemLabelGenerator generator = null;
	    if(args.getKind().equals(KIND.SHIPMENT))
	    	generator =new StandardCategoryItemLabelGenerator("{2}", new DecimalFormat("0"));
	    else
	    	generator =new StandardCategoryItemLabelGenerator("{2}", new DecimalFormat("0.00"));
	    
	    xyitem.setBaseItemLabelGenerator(generator);
	    
		File file = new File("tmp/3.jpg");
		try {
		ChartUtilities.saveChartAsJPEG(file, mChart, 740, 400);
		} catch (Exception e) {
		System.out.println("Problem occurred creating chart.");
		}

		return file; 
		
	}
	
	public void showComposite(){
//		int width= args.getWidth();
//		int height = args.getHeight();
		
		int width= 740;
		int height = 400;		
		this.setSize(width, height);
		
		
		Label lbl_title = new Label(this, SWT.BORDER);
		lbl_title.setBounds(0, 0, (int)(width/10), (int)(height/20));
		if(args.getKind().equals(KIND.SHIPMENT))
			lbl_title.setText("销量趋势");
		else
			lbl_title.setText("利润趋势");
		
//		Image image = computeTrend();			
		Composite composite_image  = new Composite(this, SWT.BORDER);
		composite_image.setBackground(new Color(this.getDisplay(), 255, 250, 250));
		composite_image.setBounds(0, (int)(height/20), 740, 400);		
		
		ImageLoader imageLoader = new ImageLoader();
		File imagefile = ComputeImage(ds);
		
		ImageData[] data;
		try {
			//C:/Users/IBM_ADMIN/Desktop/钱多多v0.1_MarkMan.png
		data = imageLoader.load(imagefile.getCanonicalPath());
		
		final Image imageScale = new Image(null, data[0].scaledTo(740, 400));
		
		Canvas canvas = new Canvas(composite_image, SWT.NONE);
		canvas.addPaintListener(new PaintListener() {
            public void paintControl(PaintEvent e) {
                if(imageScale!=null)
                    e.gc.drawImage(imageScale, 0, 0);
            }
        });		
		canvas.redraw();
		canvas.setBounds(0, 0, 740, 400);
		} catch (IOException e1) {
			e1.printStackTrace();
		}
		
		
		
	}
	/**
	 * release the source
	 */
	public void remove(){
		this.dispose();
	}
	
}

