package ds.docusheet.table;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.mixpanel.android.mpmetrics.MixpanelAPI;

import java.util.ArrayList;
import java.util.List;

public class TemplateFragment extends Fragment {
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    int posTemp=-1,posCard=-1;


    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
RecyclerView recyclerView;
TextView heading;
    public TemplateFragment() {
        // Required empty public constructor
    }
    public static TemplateFragment newInstance(String param1, String param2) {
        TemplateFragment fragment = new TemplateFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);



        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        //
        // Inflate the layout for this fragment
        View v=inflater.inflate(R.layout.fragment_template, container, false);
        MixpanelAPI mixpanel =
                MixpanelAPI.getInstance(getContext(),"0b359b9b0d40bb01e6381e29c23f1310");

        mixpanel.track("On Resume",null);
        mixpanel.flush();
        recyclerView=v.findViewById(R.id.template_recyclerView);
        List<Templates> list=new ArrayList<>();
        List<Cards> listCards=new ArrayList<>();
        listCards.add(new Cards(getResources().getString(R.string.cashbook),R.drawable.cash));
        listCards.add(new Cards(getResources().getString(R.string.payment_received),R.drawable.payment_received));
        listCards.add(new Cards(getResources().getString(R.string.daily_spend),R.drawable.payment_spend));
        listCards.add(new Cards(getResources().getString(R.string.orders),R.drawable.order));
        listCards.add(new Cards(getResources().getString(R.string.sell_register),R.drawable.shop_sell));
        listCards.add(new Cards(getResources().getString(R.string.salary_book),R.drawable.salarybook));
        listCards.add(new Cards(getResources().getString(R.string.attendance),R.drawable.attendance));
        list.add(new Templates(getResources().getString(R.string.shop),listCards));

        listCards=new ArrayList<>();
        listCards.add(new Cards(getResources().getString(R.string.topic_name),R.drawable.topic_name));
        listCards.add(new Cards(getResources().getString(R.string.homework),R.drawable.home_work_deadline));
        listCards.add(new Cards(getResources().getString(R.string.timetable),R.drawable.timetable));
        listCards.add(new Cards(getResources().getString(R.string.classnotes),R.drawable.classnotes));
        listCards.add(new Cards(getResources().getString(R.string.cashsplitting),R.drawable.cashsplit));
        listCards.add(new Cards(getResources().getString(R.string.courseplanning),R.drawable.courseplanning));
        listCards.add(new Cards(getResources().getString(R.string.importantdates),R.drawable.importantdates));
        listCards.add(new Cards(getResources().getString(R.string.examshedule),R.drawable.examschedule));
        list.add(new Templates(getResources().getString(R.string.student),listCards));


        listCards=new ArrayList<>();
        listCards.add(new Cards(getResources().getString(R.string.fee_register),R.drawable.fee_register));
        listCards.add(new Cards(getResources().getString(R.string.stationary_register),R.drawable.stationary_requirement));
        listCards.add(new Cards(getResources().getString(R.string.student_marks),R.drawable.student_marks));
        list.add(new Templates(getResources().getString(R.string.teachers),listCards));

        listCards=new ArrayList<>();
        listCards.add(new Cards(getResources().getString(R.string.grocery),R.drawable.grocery_list));
        listCards.add(new Cards(getResources().getString(R.string.daily_spend),R.drawable.payment_spend));
        list.add(new Templates(getResources().getString(R.string.household),listCards));

        listCards=new ArrayList<>();
        listCards.add(new Cards(getResources().getString(R.string.diet_plan),R.drawable.diet_chart));
        listCards.add(new Cards(getResources().getString(R.string.workout_plan),R.drawable.workout_chart));
        list.add(new Templates(getResources().getString(R.string.health_fitness),listCards));

        listCards=new ArrayList<Cards>();
        listCards.add(new Cards(getResources().getString(R.string.trip_register),R.drawable.fee_register));
        listCards.add(new Cards(getResources().getString(R.string.car_service),R.drawable.car_service));
        list.add(new Templates(getResources().getString(R.string.cabs_transport),listCards));

        listCards=new ArrayList<>();
        listCards.add(new Cards(getResources().getString(R.string.guest),R.drawable.guest_list));
        listCards.add(new Cards(getResources().getString(R.string.food_menu),R.drawable.food_menu));
        listCards.add(new Cards(getResources().getString(R.string.expenditure_list),R.drawable.expenditure_list));
        list.add(new Templates(getResources().getString(R.string.wedding),listCards));

        AdapterTemplate adapterTemplate = new AdapterTemplate(list, getContext());
        recyclerView.setAdapter(adapterTemplate);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        adapterTemplate.setOnItemClickListener(position -> {

        });


        return v;
    }
}