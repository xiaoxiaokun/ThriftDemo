namespace java match

struct Player
{
    1: i32 id,
    2: string name,
    3: i32 score
}

service Match {

  /**
   * A method definition looks like C code. It has a return type, arguments,
   * and optionally a list of exceptions that it may throw. Note that argument
   * lists and exception lists are specified using the exact same syntax as
   * field lists in struct or exception definitions.
   */

   i32 add(1:i32 id, 2:string name, 3:i32 score),

   i32 remove(1:i32 id, 2:string name)

}
