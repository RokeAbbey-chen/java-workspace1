package test;

public class Test4 {
    public static void main(String[] args) {
        int[] a = {3,2,1,5,6,4};
        int pos = 2;
        int result = fun(a, 0, a.length - 1, 2);
        System.out.println(result);
    }
    public static int fun(int[] a, int l, int r, int pos){
        int tmp = a[pos];
        a[pos] = a[l];
        a[l] = tmp;

        int pivot = a[l];
        int i = l;
        int j = r;
        while(i < j)
        {
            while(a[j] >= pivot && i < j)
                j--;
            a[i] = a[j];
            while(a[i] < pivot && i < j)
                i++;
            a[j] = a[i];
        }
        a[i] = pivot;
        return i;
    }
}
