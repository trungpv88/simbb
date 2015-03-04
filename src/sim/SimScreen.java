package sim;

import javax.microedition.rms.RecordEnumeration;
import javax.microedition.rms.RecordStoreException;
import javax.microedition.rms.RecordStoreNotOpenException;
import net.rim.device.api.ui.decor.*;
import net.rim.device.api.system.*;
import net.rim.device.api.ui.*;
import net.rim.device.api.ui.container.*;
import net.rim.device.api.ui.component.*;
import net.rim.device.api.ui.extension.component.*;

public final class SimScreen extends MainScreen implements FieldChangeListener {

	private Heightdb _db;
	private RecordEnumeration _enum;
	private Bitmap[] _bitmapArray = new Bitmap[4];
	private BitmapField _bitmapField;
	private CheckboxField checkBox1;
	private PictureScrollField _pictureScrollField;
	private BasicEditField _height;
	private static final int[] colors = new int[] { Color.LIGHTBLUE,
			Color.LIGHTSKYBLUE };
	private VerticalFieldManager horizontalPositioning;

	/**
	 * Creates a new SimScreen object
	 */
	public SimScreen() {
		super(NO_VERTICAL_SCROLL);

		// Set the displayed title of the screen
		setTitle("SimBB");

		try {
			_db = new Heightdb("xxx");
		} catch (Exception e) {
			System.out.println("xxx");
			e.printStackTrace();
		}

		// Create the centered top content
		HorizontalFieldManager topCenteredArea = new HorizontalFieldManager(
				USE_ALL_HEIGHT | USE_ALL_WIDTH | NO_HORIZONTAL_SCROLL);
		horizontalPositioning = new VerticalFieldManager(
				USE_ALL_WIDTH | NO_VERTICAL_SCROLL | Field.FIELD_VCENTER);
		topCenteredArea.add(horizontalPositioning);

		// Initialize the bitmap array
		_bitmapArray[0] = Bitmap.getBitmapResource("animals-elephant.png");
		_bitmapArray[1] = Bitmap.getBitmapResource("animals-hippo.png");
		_bitmapArray[2] = Bitmap.getBitmapResource("animals-owl.png");
		_bitmapArray[3] = Bitmap.getBitmapResource("animals-bumble_bee.png");

		// Add a bitmap field to the centered top content
		// _height = new BasicEditField("Work number: ", "", 50,
		// BasicEditField.FILTER_PHONE);
		loadRecords(horizontalPositioning, 0);

		// _height = new TextField("Artist: ", null, 20, TextField.ANY);
		// checkBox1 = new CheckboxField("xxx0", true);
		// _bitmapField = new BitmapField(_bitmapArray[0], Field.FIELD_HCENTER);

		// horizontalPositioning.add(_bitmapField);
		// horizontalPositioning.add(checkBox1);
		// horizontalPositioning.add(_height);

		// Initialize an array of scroll entries
		PictureScrollField.ScrollEntry[] entries = new PictureScrollField.ScrollEntry[4];
		entries[0] = new PictureScrollField.ScrollEntry(_bitmapArray[0], null,
				"Height");
		entries[1] = new PictureScrollField.ScrollEntry(_bitmapArray[1], null,
				"Weight");
		entries[2] = new PictureScrollField.ScrollEntry(_bitmapArray[2], null,
				"Vaccine");
		entries[3] = new PictureScrollField.ScrollEntry(_bitmapArray[3], null,
				"Photos");

		// Initialize the picture scroll field
		_pictureScrollField = new PictureScrollField(80, 64);
		_pictureScrollField.setData(entries, 0);
		_pictureScrollField
				.setHighlightStyle(PictureScrollField.HighlightStyle.ILLUMINATE_WITH_SHRINK_LENS);
		_pictureScrollField.setHighlightBorderColor(Color.RED);
		_pictureScrollField.setBackground(BackgroundFactory
				.createSolidBackground(Color.WHITE));
		_pictureScrollField.setLabelsVisible(true);
		_pictureScrollField.setCenteredLens(true);
		_pictureScrollField.setChangeListener(this);
		_pictureScrollField.setLabelsVisible(false);

		// Combine the top and bottom elements using a
		// JustifiedVerticalFieldManager
		JustifiedVerticalFieldManager bodyManager = new JustifiedVerticalFieldManager(
				topCenteredArea, _pictureScrollField, false);

		// Add the justified manager to the screen
		add(bodyManager);
	}

	private void loadRecords(VerticalFieldManager horizontalPositioning,
			int index) {
		// TODO Auto-generated method stub
		if (0 == index) {
			try {
				// Create a scrollable VerticalFieldManager
				VerticalFieldManager scrollingRegion = new VerticalFieldManager(
						VERTICAL_SCROLL | VERTICAL_SCROLLBAR);

				_enum = _db.enumerate();
				_enum.rebuild();
				int recordId = 0;
				while (_enum.hasNextElement()) {
					recordId = _enum.nextRecordId();
					Sim sim = _db.getSim(recordId);
					_height = new BasicEditField("Height: ", "", 50,
							BasicEditField.FILTER_PHONE);
					_height.setText(String.valueOf(sim.getHeight()));
					int color = colors[recordId % 2];
					scrollingRegion.add(new CustomField(color, String
							.valueOf(sim.getHeight())));
				}
				horizontalPositioning.add(scrollingRegion);

			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

	public void fieldChanged(Field field, int context) {
		if (field == _pictureScrollField) {
			// Set the centered bitmap to be that which is selected
			// in the picture scroll field.
			horizontalPositioning.deleteAll();
			int currentIndex = _pictureScrollField.getCurrentImageIndex();
			loadRecords(horizontalPositioning, currentIndex);
			// _bitmapField.setBitmap(_bitmapArray[currentIndex]);
			// checkBox1.setLabel("xxx" + String.valueOf(currentIndex));
		}
	}

	protected boolean onSavePrompt() {
		switch (Dialog.ask(Dialog.D_SAVE)) {
		case Dialog.SAVE:
			return onSave();
		case Dialog.DISCARD:
			return onDiscard();
		case Dialog.CANCEL:
			return onCancel();
		default:
			return false;
		}
	}

	protected boolean onSave() {
		System.out.println("Save");
		try {
			_db.add(Integer.parseInt(_height.getText()));
		} catch (Exception e) {
			e.printStackTrace();
			System.out.println("xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx");
		}
		return true;
	}

	protected boolean onDiscard() {
		System.out.println("Discard");
		return true;
	}

	protected boolean onCancel() {
		System.out.println("Cancel");
		return false;
	}
}
