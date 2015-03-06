package sim;

import java.util.Calendar;
import java.util.Date;

import javax.microedition.rms.RecordEnumeration;
import javax.microedition.rms.RecordStoreException;
import javax.microedition.rms.RecordStoreNotOpenException;

import net.rim.device.api.ui.decor.*;
import net.rim.device.api.lowmemory.LowMemoryListener;
import net.rim.device.api.lowmemory.LowMemoryManager;
import net.rim.device.api.system.*;
import net.rim.device.api.ui.*;
import net.rim.device.api.ui.container.*;
import net.rim.device.api.ui.component.*;
import net.rim.device.api.ui.extension.component.*;
import net.rim.device.api.ui.extension.container.EyelidFieldManager;

public final class SimBBScreen extends MainScreen implements
		FieldChangeListener, LowMemoryListener, ListFieldCallback {

	private RecordEnumeration _enum;
	private Bitmap[] _bitmapArray = new Bitmap[4];
	private BitmapField _bitmapField;
	private CheckboxField checkBox1[] = new CheckboxField[20];
	private PictureScrollField _pictureScrollField;
	private static final int[] colors = new int[] { Color.LIGHTBLUE,
			Color.LIGHTSKYBLUE };
	private VerticalFieldManager horizontalPositioning;
	final String[] MONTHS = { "January", "February", "March", "April", "May",
			"June", "July", "August", "September", "October", "November",
			"December" };
	final String[] DAYS = { "Monday", "Tuesday", "Wednesday", "Thursday",
			"Friday", "Saturday", "Sunday" };
	TextSpinBoxField spinBoxMonths;
	TextSpinBoxField spinBoxDays[] = new TextSpinBoxField[20];
	SpinBoxFieldManager spinBoxMgr;
	private DateField _date;
	private BasicEditField _height;
	private BasicEditField _weight;
	private DateField _date2;
	private BasicEditField _height2;
	private DateField _date3[] = new DateField[20];
	private BasicEditField _height3[] = new BasicEditField[20];
	// Members
	// -------------------------------------------------------------------------------------
	private SimList _simList;
	private SimListField _simListField;
	private UiApplication _app;

	private ProgressBarDialog _progressDialog;

	// Constants
	// -----------------------------------------------------------------------------------
	private static final int MAX_RECORDS = 1000;

	/**
	 * Creates a new SimScreen object
	 */
	public SimBBScreen() {
		super(NO_VERTICAL_SCROLL);

		// Set the displayed title of the screen
		setTitle("SimBB");

		// Create the centered top content
		HorizontalFieldManager topCenteredArea = new HorizontalFieldManager(
				USE_ALL_HEIGHT | USE_ALL_WIDTH | NO_HORIZONTAL_SCROLL);
		Background bg = BackgroundFactory.createBitmapBackground(Bitmap
				.getBitmapResource("height3.png"));
		topCenteredArea.setBackground(bg);
		horizontalPositioning = new VerticalFieldManager(USE_ALL_WIDTH
				| Field.FIELD_LEFT | Manager.VERTICAL_SCROLL);

		Bitmap borderBitmap = Bitmap.getBitmapResource("height3.png");
		// horizontalPositioning.setBorder(BorderFactory.createBitmapBorder(
		// new XYEdges(12, 12, 12, 12), borderBitmap));

		topCenteredArea.add(horizontalPositioning);

		// Initialize the bitmap array
		_bitmapArray[0] = Bitmap.getBitmapResource("animals-elephant.png");
		_bitmapArray[1] = Bitmap.getBitmapResource("animals-hippo.png");
		_bitmapArray[2] = Bitmap.getBitmapResource("animals-owl.png");
		_bitmapArray[3] = Bitmap.getBitmapResource("animals-bumble_bee.png");

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
		

		_app = UiApplication.getUiApplication();
		// Get and display the order list.
		_simList = SimList.getInstance();
		_simListField = new SimListField(_simList.getNumSimRecords());
		//_simListField = new SimListField(_simList.getNumSimRecordsById(_pictureScrollField.getCurrentImageIndex()));
		_simListField.setCallback(this);
		horizontalPositioning.add(_simListField);

		LowMemoryManager.addLowMemoryListener(this);
	}

	public void fieldChanged(Field field, int context) {
		if (field == _pictureScrollField) {
			// Set the centered bitmap to be that which is selected
			// in the picture scroll field.
			// horizontalPositioning.deleteAll();
			// int currentIndex = _pictureScrollField.getCurrentImageIndex();
			// loadRecords(horizontalPositioning, currentIndex);
			// _bitmapField.setBitmap(_bitmapArray[currentIndex]);
			// checkBox1.setLabel("xxx" + String.valueOf(currentIndex));
			//horizontalPositioning.deleteAll();
			//_simListField = new SimListField(_simList.getNumSimRecordsById(_pictureScrollField.getCurrentImageIndex()));
			//_simListField.setCallback(this);
			//horizontalPositioning.add(_simListField);
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

	public boolean onClose() {
		LowMemoryManager.removeLowMemoryListener(this);
		_simList.commit();

		return super.onClose();
	}

	protected boolean invokeAction(int action) {
		switch (action) {
		case ACTION_INVOKE: // Trackball click.
			viewRecord(_simListField.getSelectedIndex());
			return true; // We've consumed the event.
		}
		return super.invokeAction(action);
	}

	protected boolean keyChar(char key, int status, int time) {
		if (key == Characters.ENTER) {
			viewRecord(_simListField.getSelectedIndex());
			return true;
		}

		return super.keyChar(key, status, time);
	}

	private void viewRecord(int index) {
		SimRecord orderRecord = (SimRecord) /* outer. */get(_simListField,
				index);
		NoteScreen screen = new NoteScreen(orderRecord, false);
		/* outer. */_app.pushModalScreen(screen);
		orderRecord = screen.getUpdatedOrderRecord();

		if (orderRecord != null) {
			/* outer. */_simList.replaceOrderRecordAt(
					_simListField.getSelectedIndex(), orderRecord);
		}
	}

	public void drawListRow(ListField listField, Graphics graphics, int index,
			int y, int width) {
		System.out.println("xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx");
		System.out.println(index);
		System.out.println(y);
		Object object = get(listField, index);
		if (((SimRecord) object).getId() == _pictureScrollField.getCurrentImageIndex())
			graphics.drawText(object.toString(), 0, y, 0, width);
	}

	public int getPreferredWidth(ListField listField) {
		return Display.getWidth();
	}

	public Object get(ListField listField, int index) {
		return _simList.getSimRecordAt(index);
	}

	public int indexOfList(ListField listField, String prefix, int start) {
		return -1; // Not implemented.
	}

	public boolean freeStaleObject(int priority) {
		boolean freedData = false;

		switch (priority) {
		case LowMemoryListener.LOW_PRIORITY: {
			break;
		}

		case LowMemoryListener.MEDIUM_PRIORITY: {
			int numYearsAgo = 15;

			Calendar calendar = Calendar.getInstance();
			calendar.setTime(new Date());
			int year = calendar.get(Calendar.YEAR);
			calendar.set(Calendar.YEAR, year - numYearsAgo);
			freedData = _simList.removeStaleSimRecords(calendar.getTime()
					.getTime());
			_simListField.setSize(_simList.getNumSimRecords());

			break;
		}

		case LowMemoryListener.HIGH_PRIORITY: {
			int numYearsAgo = 10;

			Calendar calendar = Calendar.getInstance();
			calendar.setTime(new Date());
			int year = calendar.get(Calendar.YEAR);
			calendar.set(Calendar.YEAR, year - numYearsAgo);
			freedData = _simList.removeStaleSimRecords(calendar.getTime()
					.getTime());
			_simListField.setSize(_simList.getNumSimRecords());

			break;
		}
		}

		return freedData;
	}

	private final class SimListField extends ListField {
		SimListField(int numEntries) {
			super(numEntries);
		}

		public ContextMenu getContextMenu() {
			ContextMenu contextMenu = super.getContextMenu();

			if (getSize() > 0) {
				int index = getSelectedIndex();
				contextMenu.addItem(new New());
				contextMenu.addItem(new Edit(index));
				contextMenu.addItem(new Delete(index));
			} else {
				contextMenu.addItem(new New());
			}

			return contextMenu;
		}
	}

	private final class New extends MenuItem {
		private New() {
			super("Add", 99, 99);
		}

		public void run() {

			SimRecord simRecord = new SimRecord("", System.currentTimeMillis(),
					_pictureScrollField.getCurrentImageIndex());
			/* outer. */_simList.insertSimRecord(simRecord);
			/* outer. */_simListField.setSize( /* outer. */_simList
					.getNumSimRecords());
			viewRecord(0);
		}
	}

	private final class Edit extends MenuItem {
		private int _index;

		private Edit(int index) {
			super("Edit", 101, 100);
			_index = index;
		}

		public void run() {
			viewRecord(_index);
		}
	}

	private final class Delete extends MenuItem {
		private int _index;

		private Delete(int index) {
			super("Delete", 103, 103);
			_index = index;
		}

		public void run() {
			if (Dialog.ask(Dialog.D_DELETE) == Dialog.DELETE) {

				SimRecord orderRecord = (SimRecord) /* outer. */get(
						_simListField, _index);
				/* outer. */_simList.deleteSimRecord(orderRecord);
				/* outer. */_simListField.setSize( /* outer. */_simList
						.getNumSimRecords());
			}
		}
	}

	static class ProgressBarDialog implements CountAndSortListener {
		private DialogFieldManager _manager;
		private PopupScreen _popupScreen;
		private GaugeField _gaugeField;
		private LabelField _lbfield;

		private int _max;
		private int _stepSize;

		private ProgressBarDialog(String title, int max) {
			_max = max; // Number of records to be added.

			// Make sure that step size is at least one.
			_stepSize = Math.max(_max / 100, 1);

			_manager = new DialogFieldManager();
			_popupScreen = new PopupScreen(_manager);
			_gaugeField = new GaugeField(null, 0, max, 0, GaugeField.PERCENT);
			_lbfield = new LabelField(title, Field.USE_ALL_WIDTH);

			_manager.addCustomField(_lbfield);
			_manager.addCustomField(_gaugeField);

			UiApplication.getUiApplication().pushScreen(_popupScreen);
		}

		public void counterUpdated(int counter) {
			if (counter % _stepSize == 0) {
				_gaugeField.setValue(counter + 1);
			}
		}

		public void sortingStarted() {
			// Remove _gaugeField and change the text displayed on _popupScreen
			// to
			// "Sorting records..." .
			UiApplication.getUiApplication().invokeLater(new Runnable() {
				public void run() {
					_manager.deleteCustomField(_gaugeField);
					_lbfield.setText("Sorting records...");
				}
			});
		}

		public void sortingFinished() {
			// Remove _popupScreen from the stack.
			UiApplication.getUiApplication().invokeLater(new Runnable() {
				public void run() {
					UiApplication.getUiApplication().popScreen(_popupScreen);
				}
			});
		}
	}
}

interface CountAndSortListener {
	/**
	 * Called when the counter is updated
	 * 
	 * @param counter
	 *            The new counter
	 */
	public void counterUpdated(int counter);

	/**
	 * Called when sorting is started
	 */
	public void sortingStarted();

	/**
	 * Called when sorting is finished
	 */
	public void sortingFinished();
}