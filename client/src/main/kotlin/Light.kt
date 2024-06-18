import vision.gears.webglmath.UniformProvider
import vision.gears.webglmath.Vec2
import vision.gears.webglmath.Vec3
import vision.gears.webglmath.Vec4

open class Light(id : Int, vararg programs : Program) : UniformProvider("lights[$id]") {

  val position by Vec4(0.0f, 1.0f, 0.0f, 0.0f) 
  val powerDensity by Vec3(0.0f, 0.0f, 0.0f)
  val direction by Vec3(0.0f, -1.0f, 0.0f)
  val exponentAndAngle by Vec2(2.0f, 40.0f)
    
  init{
    addComponentsAndGatherUniforms(*programs)
  }

}