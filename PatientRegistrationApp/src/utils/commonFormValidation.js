export default {
  photoSize: (v) =>{
    if(v && v.size > 2097152) {
      return 'Image size should be less than 2 MB!';
    }else{
      return true;
    }
  }
}
