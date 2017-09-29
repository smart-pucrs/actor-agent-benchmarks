#include <stdio.h>
#include <ctype.h>
#include <string.h>

int main() {
  char line[100];
  int i;
  
  scanf("%[^\n]", line);
  scanf("%*c");
  line[0] = '\0';

  while (scanf("%[^\n]", line) != EOF) {
    
    for (i = 0; ; i++)
      if (line[i] == ']') break;
        
    for (i+=2;;i++) {
       if (line[i] == '+') break;
       
       printf("%c",line[i]);
    }
    printf(",%s\n",&line[++i]);
    
    scanf("%*c");
    line[0] = '\0';
  }
  
  return 0;
}
