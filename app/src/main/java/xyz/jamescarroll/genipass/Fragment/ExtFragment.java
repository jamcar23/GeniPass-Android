package xyz.jamescarroll.genipass.Fragment;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.TypedValue;
import android.view.View;
import android.widget.EditText;

import xyz.jamescarroll.genipass.R;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link ExtFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 */
public abstract class ExtFragment extends Fragment implements View.OnClickListener {
    public static final String TAG = "ExtFragment.TAG";

    protected OnFragmentInteractionListener mListener;

    public ExtFragment() {
        // Required empty public constructor
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        findView(R.id.fab).setOnClickListener(this);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    private int getDimension(int id) {
        return (int) getResources().getDimension(id);
    }

    protected View setPadding(View v) {
        TypedValue tv = new TypedValue();

        if (getActivity().getTheme().resolveAttribute(android.R.attr.actionBarSize, tv, true)) {
            v.setPadding(getDimension(R.dimen.activity_vertical_margin),
                    TypedValue.complexToDimensionPixelSize(tv.data, getResources().getDisplayMetrics())
                            + getDimension(R.dimen.activity_horizontal_margin),
                    getDimension(R.dimen.activity_vertical_margin),
                    getDimension(R.dimen.activity_horizontal_margin));
        }

        return v;
    }

    protected View findView(int id) {
        return getActivity().findViewById(id);
    }

    protected String getTextFromEditText(int view) {
        return ((EditText) findView(view)).getText() + "";
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p/>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        void onFragmentInteraction(Intent frag);
    }

    public static String getTAG() {
        return TAG;
    }
}