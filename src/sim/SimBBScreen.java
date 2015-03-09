/**
 * Based on 'Wire Frame Layout Demo'
 * and 'Memory Demo' samples 
 * http://developer.blackberry.com/
 */

package sim;

import java.util.Calendar;
import java.util.Date;

import net.rim.device.api.ui.Font;
import net.rim.device.api.ui.FontFamily;
import net.rim.device.api.ui.decor.*;
import net.rim.device.api.lowmemory.LowMemoryListener;
import net.rim.device.api.lowmemory.LowMemoryManager;
import net.rim.device.api.system.*;
import net.rim.device.api.ui.*;
import net.rim.device.api.ui.container.*;
import net.rim.device.api.ui.component.*;
import net.rim.device.api.ui.extension.component.*;

public final class SimBBScreen extends MainScreen implements
		FieldChangeListener, LowMemoryListener, ListFieldCallback {

	private Bitmap[] _bitmapArray = new Bitmap[4];
	private PictureScrollField _pictureScrollField;
	private VerticalFieldManager horizontalPositioning;
	private SimList _simList;
	private SimListField _simListField;
	private UiApplication _app;

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
		horizontalPositioning = new VerticalFieldManager(USE_ALL_WIDTH
				| Field.FIELD_LEFT | Manager.VERTICAL_SCROLL);
		Bitmap borderBitmap = Bitmap.getBitmapResource("rounded-border.png");
		horizontalPositioning.setBorder(BorderFactory.createBitmapBorder(
				new XYEdges(12, 12, 12, 12), borderBitmap));
		topCenteredArea.add(horizontalPositioning);

		// Initialize the bitmap array
		_bitmapArray[0] = Bitmap.getBitmapResource("animals-elephant.png");
		_bitmapArray[1] = Bitmap.getBitmapResource("animals-hippo.png");
		_bitmapArray[2] = Bitmap.getBitmapResource("animals-owl.png");
		_bitmapArray[3] = Bitmap.getBitmapResource("animals-bumble_bee.png");

		// Initialize an array of scroll entries
		PictureScrollField.ScrollEntry[] entries = new PictureScrollField.ScrollEntry[4];
		entries[0] = new PictureScrollField.ScrollEntry(_bitmapArray[0], null,
				"Weight");
		entries[1] = new PictureScrollField.ScrollEntry(_bitmapArray[1], null,
				"Height");
		entries[2] = new PictureScrollField.ScrollEntry(_bitmapArray[2], null,
				"Vaccine");
		entries[3] = new PictureScrollField.ScrollEntry(_bitmapArray[3], null,
				"Notes");

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
		// Get and display the record list.
		_simList = SimList.getInstance();
		_simListField = new SimListField(
				_simList.getNumSimRecordsById(_pictureScrollField
						.getCurrentImageIndex()));
		_simListField.setCallback(this);
		horizontalPositioning.add(_simListField);

		LowMemoryManager.addLowMemoryListener(this);
	}

	public void fieldChanged(Field field, int context) {
		if (field == _pictureScrollField) {
			horizontalPositioning.deleteAll();
			_simListField = new SimListField(
					_simList.getNumSimRecordsById(_pictureScrollField
							.getCurrentImageIndex()));
			_simListField.setCallback(this);
			horizontalPositioning.add(_simListField);
		}
	}

	public boolean onClose() {
		LowMemoryManager.removeLowMemoryListener(this);
		_simList.commit();

		return super.onClose();
	}

	protected boolean invokeAction(int action) {
		switch (action) {
		case ACTION_INVOKE: // Trackball click.
			if (_simList.getNumSimRecordsById(_pictureScrollField
					.getCurrentImageIndex()) > 0)
				viewRecord(_simList.particularToCommonFieldIndex(
						_pictureScrollField.getCurrentImageIndex(),
						_simListField.getSelectedIndex()), false);
			return true; // We've consumed the event.
		}
		return super.invokeAction(action);
	}

	protected boolean keyChar(char key, int status, int time) {
		if (key == Characters.ENTER) {
			if (_simList.getNumSimRecordsById(_pictureScrollField
					.getCurrentImageIndex()) > 0)
				viewRecord(_simList.particularToCommonFieldIndex(
						_pictureScrollField.getCurrentImageIndex(),
						_simListField.getSelectedIndex()), false);
			return true;
		}

		return super.keyChar(key, status, time);
	}

	private void viewRecord(int index, boolean editable) {
		SimRecord orderRecord = (SimRecord) /* outer. */get(_simListField,
				index);
		NoteScreen screen = new NoteScreen(orderRecord, editable);
		/* outer. */_app.pushModalScreen(screen);
		orderRecord = screen.getUpdatedOrderRecord();

		if (orderRecord != null) {
			/* outer. */_simList.replaceOrderRecordAt(index, orderRecord);
		}
	}

	public void drawListRow(ListField listField, Graphics graphics, int index,
			int y, int width) {
		int currentId = _pictureScrollField.getCurrentImageIndex();
		Object object = get(listField,
				_simList.particularToCommonFieldIndex(currentId, index));
		int paddingLeft = 50;
		int paddingTop = 4;
		int paddingRight = 10;
		int lineHeight = 25;
		int dateTimeWidth = 100;
		int fontSize = 20;
		if (((SimRecord) object).getId() == currentId) {
			try {
				FontFamily fontFamily = FontFamily.forName("BBJapanese");
				Font font = fontFamily.getFont(Font.PLAIN, fontSize);
				graphics.setFont(font);

				graphics.setColor(Color.RED);
				String recordString = object.toString();
				String dateTimeString = recordString.substring(0, 11);
				graphics.drawText(dateTimeString, paddingLeft, y + paddingTop,
						0, width);

				graphics.setColor(Color.BLACK);
				String noteString = recordString.substring(12,
						recordString.length());
				graphics.drawText(noteString, paddingLeft + dateTimeWidth, y
						+ paddingTop, 0, width);

				graphics.setColor(Color.BURLYWOOD);
				graphics.drawLine(paddingLeft, y + lineHeight, width
						- paddingRight, y + lineHeight);

				Bitmap bitmap = Bitmap.getBitmapResource("xp.png");
				graphics.drawBitmap(paddingTop, y + paddingTop,
						bitmap.getWidth(), bitmap.getHeight(), bitmap, 0, 0);
			} catch (ClassNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		}
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
				contextMenu.addItem(new Insert(index));
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
			super("Add", 98, 98);
		}

		public void run() {

			SimRecord simRecord = new SimRecord("", System.currentTimeMillis(),
					_pictureScrollField.getCurrentImageIndex());
			/* outer. */_simList.addSimRecord(simRecord);
			/* outer. */_simListField.setSize( /* outer. */_simList
					.getNumSimRecordsById(_pictureScrollField
							.getCurrentImageIndex()));
			viewRecord(0, true);
		}
	}

	private final class Insert extends MenuItem {
		private int _index;

		private Insert(int index) {
			super("Insert", 99, 99);
			_index = index;
		}

		public void run() {

			SimRecord simRecord = new SimRecord("", System.currentTimeMillis(),
					_pictureScrollField.getCurrentImageIndex());
			int pos = _simList.particularToCommonFieldIndex(
					_pictureScrollField.getCurrentImageIndex(), _index);
			/* outer. */_simList.insertSimRecord(simRecord, pos);
			/* outer. */_simListField.setSize( /* outer. */_simList
					.getNumSimRecordsById(_pictureScrollField
							.getCurrentImageIndex()));
			viewRecord(pos, true);
		}
	}

	private final class Edit extends MenuItem {
		private int _index;

		private Edit(int index) {
			super("Edit", 101, 100);
			_index = index;
		}

		public void run() {
			viewRecord(
					_simList.particularToCommonFieldIndex(
							_pictureScrollField.getCurrentImageIndex(), _index),
					true);
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
						_simListField, _simList.particularToCommonFieldIndex(
								_pictureScrollField.getCurrentImageIndex(),
								_index));
				/* outer. */_simList.deleteSimRecord(orderRecord);
				/* outer. */_simListField.setSize( /* outer. */_simList
						.getNumSimRecordsById(_pictureScrollField
								.getCurrentImageIndex()));
			}
		}
	}
}