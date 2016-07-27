package adapter;

import java.util.Comparator;

import object.SecondInfo;

/**
 * Created by cmcm1 on 2016-07-24.
 */
public class CompareRule implements Comparator<Object>
{

    public int compare(Object aObj1, Object aObj2)
    {

        // TODO Auto-generated method stub
        SecondInfo r1 = (SecondInfo) aObj1;

        SecondInfo r2 = (SecondInfo) aObj2;


        if (r1.getName().compareTo(r2.getName()) >0)
            return 1;

        else if (r1.getName().compareTo(r2.getName()) <0)
            return -1;

        else

            return 0;

    }

}
