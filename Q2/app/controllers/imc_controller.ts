import type { HttpContext } from '@adonisjs/core/http'

export default class ImcController {
  public async calculate({ view, request }: HttpContext) {
    let { weight, height } = request.all()
    const imc = weight / (height * height)

    return view.render('pages/home', { imc })
  }
}
