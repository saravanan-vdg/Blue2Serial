package com.macroyau.blue2serial;

import android.bluetooth.BluetoothDevice;
import android.content.Context;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.afollestad.materialdialogs.MaterialDialog;
import com.afollestad.materialdialogs.Theme;

import java.util.Set;

/**
 * Dialog for selecting a remote Bluetooth device themed with the Material Design style.
 * Credit to Aidan Follestad's Material Dialogs library (https://github.com/afollestad/material-dialogs).
 *
 * @author Macro Yau
 */
public class BluetoothDeviceListDialog {

    /**
     * Listener for the {@link com.macroyau.blue2serial.BluetoothDeviceListDialog}.
     */
    public static interface OnDeviceSelectedListener {

        /**
         * A remote Bluetooth device is selected from the dialog.
         *
         * @param device The selected device.
         */
        void onBluetoothDeviceSelected(BluetoothDevice device);

    }

    private Context mContext;
    private OnDeviceSelectedListener mListener;
    private Set<BluetoothDevice> mDevices;
    private String[] mNames, mAddresses;
    private String mTitle;
    private boolean mShowAddress = true;
    private boolean mUseDarkTheme;

    /**
     * Constructor.
     *
     * @param context The {@link android.content.Context} to use.
     */
    public BluetoothDeviceListDialog(Context context) {
        mContext = context;
    }

    /**
     * Set a listener to be invoked when a remote Bluetooth device is selected.
     *
     * @param listener The {@link com.macroyau.blue2serial.BluetoothDeviceListDialog.OnDeviceSelectedListener} to use.
     */
    public void setOnDeviceSelectedListener(OnDeviceSelectedListener listener) {
        mListener = listener;
    }

    /**
     * Set the title of the dialog.
     *
     * @param title The title string.
     */
    public void setTitle(String title) {
        mTitle = title;
    }

    /**
     * Set the title of the dialog.
     *
     * @param resId The resource ID of the title string.
     */
    public void setTitle(int resId) {
        mTitle = mContext.getString(resId);
    }

    /**
     * Set the remote Bluetooth devices to be shown on the dialog for selection.
     *
     * @param devices The remote Bluetooth devices.
     */
    public void setDevices(Set<BluetoothDevice> devices) {
        mDevices = devices;

        if (devices != null) {
            mNames = new String[devices.size()];
            mAddresses = new String[devices.size()];
            int i = 0;
            for (BluetoothDevice d : devices) {
                mNames[i] = d.getName();
                mAddresses[i] = d.getAddress();
                i++;
            }
        }
    }

    /**
     * Show the devices' MAC addresses on the dialog.
     *
     * @param showAddress Set to true to show the MAC addresses.
     */
    public void showAddress(boolean showAddress) {
        mShowAddress = showAddress;
    }

    /**
     * Force to use the dark version of Material theme on the dialog.
     *
     * @param useDarkTheme Set to true to use the dark theme.
     */
    public void useDarkTheme(boolean useDarkTheme) {
        mUseDarkTheme = useDarkTheme;
    }

    /**
     * Show the dialog. This must be called after setting the dialog's listener, title and devices.
     */
    public void show() {
        final MaterialDialog dialog = new MaterialDialog.Builder(mContext)
                .title(mTitle)
                .adapter(new BluetoothDeviceListItemAdapter(mContext, mNames, mAddresses, mShowAddress))
                .theme(mUseDarkTheme ? Theme.DARK : Theme.LIGHT)
                .build();

        final ListView listView = dialog.getListView();
        if (listView != null) {
            listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    mListener.onBluetoothDeviceSelected(BluetoothSerial.getAdapter(mContext).getRemoteDevice(mAddresses[position]));
                    dialog.cancel();
                }
            });
        }

        dialog.show();
    }

}
