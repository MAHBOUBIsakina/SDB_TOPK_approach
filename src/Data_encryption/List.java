package data_encryption;

//import proposition_1_quicksort_optimise.*;
//import proposition_1_optimisee.*;
//import proposition_1_hash_function_byte.*;
//import propo1_pqt_tll_eg_vector2.*;
//import propo1_pqt_tll_eg_vector.*;
//import proposition1_equal_size_packets.*;




public class List {
  public ListElement [] elements;

  public void sortElements(int n) {
    int i,j;
    ListElement tempElement;
    int maxIndex;
    for (i=0; i<n; ++i) {
      maxIndex = i;
      for (j=i+1; j<n; ++j) {
        if (elements[j].score > elements[maxIndex].score)
          maxIndex = j;
      }
      if (maxIndex > i) {
        tempElement = elements[i];
        elements[i] = elements[maxIndex];
        elements[maxIndex] = tempElement;
      }
    }
  }

  public void sortLimitedIntScoreLists(int n, int limit)
  {
    int [] buckets = new int [limit + 1];
    int i, j, u;
    for (j = 0; j < limit + 1; ++j)
      buckets[j] = 0;
    for (i = 0; i < n; ++i)
    {
      j = (int) elements[i].score;
      buckets[j] = buckets[j] + 1;
    }
    int tempInt1, tempInt2;
    tempInt1 = buckets[0];
    buckets[0] = 0;
    for (j = 1; j < limit + 1; ++j)
    {
      tempInt2 = buckets[j];
      buckets[j] = tempInt1 + buckets[j-1];
      tempInt1 = tempInt2;
    }
    ListElement [] elems = new ListElement[n];
    for (i = 0; i < n; ++i)
    {
      elems[i] = new ListElement();
    }

    for (i = 0; i < n; ++i)
    {
      j = (int) elements[i].score;
      u = buckets[j];
      elems[u] = elements[i];
      buckets[j] = buckets[j] + 1;
    }
    for (i = 0; i < n; ++i)
    {
      elements[i] = elems[n - i - 1];
    }

  }

}
