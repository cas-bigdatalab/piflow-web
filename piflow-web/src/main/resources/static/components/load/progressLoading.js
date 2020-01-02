(function() {
  $(document).ready(function() {
    return $(".bar").each(function(i, elem) {
      return $(elem).addClass('active');
    });
  });

}).call(this);
