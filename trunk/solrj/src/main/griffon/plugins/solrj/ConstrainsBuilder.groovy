
package griffon.plugins.solrj

/* Here there are all possible expressions that could be used in our solrj criteria */
class ConstrainsBuilder {

	def terms = []
		
	/* Equals */
	void eq(name,value){
		terms << (name << ':'  << value)
	}
	
	/* Greater than */
	void gt(name,value){
		terms << (name << ':'  << "[" << value << " TO *" << "]")
	}
	
	/* Less than */
	void lt(name,value){
		terms << (name << ':'  << "[* TO " << value << "]")
	}
	
	Object build(closure){
		this.with closure
		terms
	}
	
	/* Ilike. Right now like and ilike are considered the same. % character is replaced by the expression '*' */
	void ilike(name,value){
		terms << (name << ':'  << value.replace('%','*'))
	}
	
	/* Like. Right now like and ilike are considered the same. % character is replaced by the expression '*' */
	void like(name,value){
		ilike(name,value)
	}
}
