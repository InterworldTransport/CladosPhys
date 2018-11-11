/*
 * <h2>Copyright</h2> © 2018 Alfred Differ.<br>
 * ------------------------------------------------------------------------ <br>
 * ---com.interworldtransport.cladosG.FrameRealF<br>
 * -------------------------------------------------------------------- <p>
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as
 * published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version. 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Affero General Public License for more details.<p>
 * 
 * Use of this code or executable objects derived from it by the Licensee 
 * states their willingness to accept the terms of the license. <p> 
 * 
 * You should have received a copy of the GNU Affero General Public License
 * along with this program.  If not, see <https://www.gnu.org/licenses/>.<p> 
 * 
 * ------------------------------------------------------------------------ <br>
 * ---com.interworldtransport.cladosG.FrameRealF<br>
 * ------------------------------------------------------------------------ <br>
 */
package com.interworldtransport.cladosPhys;

import java.util.ArrayList;
import com.interworldtransport.cladosG.AlgebraAbstract;

import com.interworldtransport.cladosPhysExceptions.CladosFrameException;

/**
 * The frame object holds all basis details that support the reference frame for
 * a multivector over a division field {Cl(p,q) x DivField}.
 * 
 * @version 1.0
 * @author Dr Alfred W Differ
 */
public class FrameRealF extends FrameAbstract
{
	/**
	 * Return an integer pointing to the part of the nyad that covers the
	 * algebra named in the parameter. Coverage is true if a monad can be found
	 * in the nyad that belongs to the algebra.
	 * 
	 * @param pRF
	 *            FrameRealF
	 * @param pMonad
	 *            MonadRealF
	 * @return int
	 */
	public static short findMonad(FrameRealF pRF, MonadRealF pMonad)
	{
		ArrayList<MonadRealF> testFBasis=pRF.getFBasis();
		for (MonadRealF pM : testFBasis)
			if (pM.isGEqual(pMonad)) return (short) pRF.fBasis.indexOf(pM);
		return -1;
	}

	/**
	 * Return a boolean stating whether or not the nyad covers the algebra named
	 * in the parameter. Coverage is true if a monad can be found in the nyad
	 * that belongs to the algebra.
	 * 
	 * @param pRF
	 *            FrameRealF
	 * @param pMonad
	 *            MonadRealF
	 * @return boolean
	 */
	public static boolean hasMonad(FrameRealF pRF, MonadRealF pMonad)
	{
		ArrayList<MonadRealF> testFBasis=pRF.getFBasis();
		for (MonadRealF pM : testFBasis)
			if (pM.isGEqual(pMonad)) return true;
		return false;
	}

	public static boolean isREqual(FrameRealF pRF1, FrameRealF pRF2)
	{

		// Check to see if the Algebras match
		if (pRF1.getAlgebra() != pRF2.getAlgebra()) return false;

		// Check first to see if the Frames are of the same order. Return false
		// if they are not.
		if (pRF1.getFrameOrder() != pRF2.getFrameOrder()) return false;

		// Now check the monad lists.
		boolean check = false;
		for (MonadRealF tSpot : pRF1.getFBasis())
		{
			check = false;
			String tName1 = tSpot.getName();
			for (MonadRealF tSpot2 : pRF2.getFBasis())
			{
				if (tName1.equals(tSpot2.getName()))
				{
					check = true;
					if (!tSpot.isGEqual(tSpot2)) return false;
					break;
				}
			}
			// if check is true a match was found (by monad name)
			// if check is false, we have a dangling monad (by monad name),
			// so they can't be equal.
			if (!check) return false;
		}
		// To get this far, all Monads in one list must pass the equality
		// test for their counterparts (by name) in the other list.
		return true;
	}
	
	/**
	 * Display XML string that represents the Frame
	 * @param pM
	 * 		FrameRealF This is the Frame to be converted to XML.
	 * @return String
	 */
	public static String toXMLString(FrameRealF pM)
	{
		StringBuffer rB = new StringBuffer("<Frame name=\"" + pM.getName()
						+ "\" ");
		rB.append("algebra=\"" + pM.getAlgebra().getAlgebraName() + "\" ");
		rB.append("frame size=\"" + pM.getFBasis().size()+ "\" ");
		rB.append(">\n");

		rB.append(AlgebraRealF.toXMLString((AlgebraRealF)pM.getAlgebra()));
		//rB.append("\n");
		
		for (short i=0; i<pM.getFBasis().size(); i++)
			rB.append(MonadRealF.toXMLString(pM.getFBasis(i)));
		
		rB.append("</Frame>\n");
		return rB.toString();
	}
	
	/**
	 * The fBasis holds vector monads that represent the reference directions to
	 * be used by any monad that refers to this frame object. Multiplication and
	 * addition in the monad are performed relative to these reference
	 * directions. Addition at the monad level can be done directly with no
	 * reference to the default basis in the algebra. Multiplication, however,
	 * is handed off to the reference frame since the product of two reference
	 * directions might result in a combination result.
	 * <p>
	 * The monads in the frame use the default basis constructed from the
	 * generators in the algebra. This is represented by the fact that the
	 * reference monads refer to the frame to which they are attached.
	 * Multiplication at the frame level never calls on the multiplication of
	 * the reference monads, so there is no danger of a loop occurring. The frame
	 * must be able to resolve operations without calling on the operations of
	 * the reference monads.
	 * 
	 */
	protected ArrayList<MonadRealF>	fBasis;
	
	/**
	 * The reciprocal frame can be referenced from here if it is known. There is
	 * no plan to construct one automatically from this frame.
	 */
	protected FrameRealF			reciprocal;
		
	/**
	 * Frame constructor with an empty basis list.
	 * 
	 * @param pName
	 * 			This is the name of the Frame being constructed
	 * @param pAlg
	 * 			This is the algebra referenced in the Frame
	 */
	public FrameRealF(String pName, AlgebraRealF pAlg)
	{
		setName(pName);
		setAlgebra(pAlg);
		fBasis = new ArrayList<MonadRealF>(getAlgebra().getGBasis().getBladeCount() - 1);
		setNameList(null);
	}
	
	/**
	 * Frame Constructor with a full basis list.
	 * 
	 * @param pName
	 *            String
	 * @param pAlg
	 *            AlgebraRealF
	 * @param pML
	 *            ArrayList List contains MonadRealF entries used in construction.
	 */
	public FrameRealF(	String pName, 
						AlgebraRealF pAlg, 
						ArrayList<MonadRealF> pML)
	{
		setName(pName);
		setAlgebra(pAlg);
		fBasis = new ArrayList<MonadRealF>(pML);
	}


	/**
	 * Add another Monad to the list of monads in this frame. This method does
	 * not create a new copy of the Monad offered as a parameter. The Frame DOES
	 * wind up referencing the passed Monad.
	 * 
	 * There are a few things to check before a the offered Monad should be
	 * appended to the Frame.
	 * 1)	Is it sharing the same algebra as the frame? No? Reject.
	 * 2)	Is it arriving with its own non-default frame? Yes? Reject.
	 * 3)	Is the fBasis already full? Yes? Reject.
	 * 
	 * 
	 * @param pM
	 *            MonadRealF this is the referenced monad for the Frame.
	 * @throws CladosFrameException
	 */
	/*
	public void appendNamedMonad(MonadRealF pM) throws CladosFrameException
	{
		// This method works if the foot of pM matches the foot of this frame
		// and the algebra of pM matches the one for this frame.
		// It is enough to ensure they share the same algebra. This is Reference Match.
		if (!pM.getAlgebra().equals(getAlgebra()))
			throw new CladosFrameException(this, "Monads in a Frame must have algebras match");
		
		// This method is not equipped to handle recursion through frames
		// so the Monad being appended must use the default frame for itself.
		if (!pM.getFrame().equals(null))
			throw new CladosFrameException(this, "Monads in a Frame must use default frame for themselves");
			
		// fBasis is essentially a list of generators of the Frame. Appending another
		// Monad should be prevented if fBasis is already full.
		if (isFilled(this))
			throw new CladosFrameException(this, "Frame generator basis is already filled");
		
		// Add Monad to the ArrayList
		fBasis.ensureCapacity(fBasis.size() + 1);

		// The offered Monad is given this Frame for a name, but it's 'frame' is left empty.
		// This signifies it uses the default 'frame', but with this name as an alias.
		pM.setFrameName(this.name);
	
		// The offered Monad is finally added to the end of the ArrayList.
		// This should cause the Frame to rethink the spaces it spans by recalculating its 'blades'.
		fBasis.add(new MonadRealF(pM));
		//reBlade(this);

	}
	*/

	/**
	 * Return the array of Monads used as the frame's basis. 
	 * This basically just hands the whole thing over for another object to mangle.
	 * DANGER
	 * 
	 * @return ArrayList (of Monads)
	 */
	@Override
	public ArrayList<MonadRealF> getFBasis()
	{
		return fBasis;
	}

	/**
	 * Return the element of the array of Monads at the jth index.
	 * This basically just hands it over for another object to mangle.
	 * DANGER
	 * 
	 * @param pj
	 *            int
	 * @return MonadRealF
	 */
	public MonadRealF getFBasis(short pj)
	{
		return fBasis.get(pj);
	}

	/**
	 * Return the element of the array of Monads with the name pName.
	 * This basically just finds it by name then hands it over 
	 * for another object to mangle.
	 * DANGER
	 * 
	 * @param pName
	 *            String Name of the monad to be found and an index returned.
	 * @return MonadRealF
	 */
	public MonadRealF getNameBasis(String pName)
	{
		int tSpot = FrameAbstract.findName(this, pName);
		return fBasis.get(tSpot);
	}

	/**
	 * Monad leftside multiplication: (pM, index direction). The Frame resolves
	 * what monad would result if the product was between pM and a monad with a
	 * single blade described by the indexed direction. The indexed monad
	 * happens to be in the fBasis list at that index.
	 * <p>
	 * Multiplication between pM
	 * 
	 * @param pReferenceIndex
	 *            short
	 * @param pM
	 *            MonadRealF
	 * @return MonadRealF
	 */
	protected MonadRealF multiplyLeft(short pReferenceIndex, MonadRealF pM)
	{
		return null;
	}
	
	/**
	 * Monad rightside multiplication: (index direction, pM). The Frame resolves
	 * what monad would result if the product was between pM and a monad with a
	 * single blade described by the indexed direction. The indexed monad
	 * happens to be in the fBasis list at that index.
	 * <p>
	 * Multiplication between pM
	 * 
	 * @param pReferenceIndex
	 *            short
	 * @param pM
	 *            MonadRealF
	 * @return MonadRealF
	 */
	protected MonadRealF multiplyRight(short pReferenceIndex, MonadRealF pM)
	{
		return null;
	}

	//@Override
	protected void orthogonalizeOn(MonadRealF pM)
	{

	}

	/**
	 * Remove a Monad on the list of monads in this nyad.
	 * 
	 * @param pthisone
	 *            short
	 * @throws CladosFrameException 
	 * 	Monad removal failed. Couldn't find it.
	 */
	protected void removeNamedMonad(short pthisone) throws CladosFrameException
	{
		MonadRealF test = null;
		try
		{
			test = fBasis.remove(pthisone);
		}
		catch (IndexOutOfBoundsException e)
		{
			throw new CladosFrameException(this, 
					"Specific Monad removal at ["+ pthisone + "] didn't work.");
		}
		finally
		{
			if (test != null) fBasis.trimToSize();
		}
	}

	/**
	 * Remove a Monad on the list of monads in this nyad.
	 * 
	 * @param pM
	 *            MonadRealF
	 * @throws CladosFrameException 
	 * 	Happens when removal fails.
	 */
	protected void removeRefMonad(MonadRealF pM) throws CladosFrameException
	{
		short testfind = findMonad(this, pM);
		if (testfind >= 0)
			removeNamedMonad(testfind);
		else
			throw new CladosFrameException(this,
							"Can't find the Monad to remove.");
	}

	/**
	 * Set the Monad List array of this FrameRealF.
	 * 
	 * @param pML
	 * 			This is the monad array passed to create this frame.
	 */
	public void setFBasis(ArrayList<MonadRealF> pML)
	{
		if (pML == null)
			fBasis = null;
		else
			fBasis = pML;

	}
}
