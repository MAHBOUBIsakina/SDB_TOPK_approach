package data_encryption;

//import proposition_1_quicksort_optimise.*;
//import proposition_1_optimisee.*;
//import proposition_1_hash_function_byte.*;
//import propo1_pqt_tll_eg_vector2.*;
//import propo1_pqt_tll_eg_vector.*;
//import proposition1_equal_size_packets.*;



public class AnswerElement {
  public String dataID;
  public double PeriScoreinf;
  public double periscoresup;
  public double [] local_scores ;
  public AnswerElement (int numc){
       local_scores=new double[numc] ;
  }
}
